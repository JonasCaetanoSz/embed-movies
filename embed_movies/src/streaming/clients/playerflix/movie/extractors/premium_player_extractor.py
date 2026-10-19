import asyncio
import json
from pathlib import Path
import re
from typing import TYPE_CHECKING

from nodriver import cdp
import nodriver as uc
import requests

from .base import BasePlayerExtractor

from ..models.video_source import VideoSource

if TYPE_CHECKING:
    from ..models import Player


SESSION_DIR = Path(__file__).parent / "browser_session"


class Browser:

    def __init__(self, player: "Player", headers: dict):
        self.player = player
        self.headers = headers
        self.browser = None
        self.tab = None


    async def start(self):
        SESSION_DIR.mkdir(parents=True, exist_ok=True)

        user_agent = (
            self.headers.get("capcha_page", {}).get("user-agent")
            or self.headers.get("capcha_page", {}).get("User-Agent")
        )

        browser_args = ["--window-size=1200,800"]

        if user_agent:
            browser_args.append(f"--user-agent={user_agent}")

        self.browser = await uc.start(
            headless=True,
            user_data_dir=str(SESSION_DIR.resolve()),
            browser_args=browser_args,
        )

        self.tab = await self.browser.get(self.player.embed)


    async def get_cookies(self) -> list[dict]:
        if self.tab is None:
            await self.start()

        try:
            await asyncio.wait_for(
                self._wait_for_embed(),
                timeout=180,
            )

            await self.tab.send(cdp.network.enable())
            await asyncio.sleep(2)

            cookies = await self.tab.send(
                cdp.network.get_all_cookies()
            )

            return [
                {
                    "name": cookie.name,
                    "value": cookie.value,
                    "domain": cookie.domain,
                    "path": cookie.path,
                    "secure": cookie.secure,
                    "httpOnly": cookie.http_only,
                }
                for cookie in cookies
            ]

        except asyncio.TimeoutError as exc:
            raise TimeoutError("Tempo limite excedido o capcha não foi resolvido") from exc

    async def _wait_for_embed(self):
        while True:
            title = await self.tab.evaluate("document.title")

            if title and "Embed" in title:
                return

            await asyncio.sleep(0.5)

    async def close(self):
        if self.browser is None:
            return

        browser = self.browser
        self.browser = None
        self.tab = None

        try:
            await browser.aclose()
        finally:
            try:
                browser.stop()
            except Exception:
                pass


class PremiumPlayerExtractor(BasePlayerExtractor):

    def extract(self, player: "Player") -> VideoSource:

        headers_path = Path(__file__).parent / "premium_player_headers.json"

        with open(headers_path, "r", encoding="utf-8") as file:
            headers = json.load(file)

        BASE_URL = "https://superflixapi.fun"

        # extraindo cookies


        loop = uc.loop()

        cookies = loop.run_until_complete( self._extract(player, headers) )

        if not cookies:
            return

        cloudflare_cookies = [

            {"name": cookie["name"], "value": cookie["value"]}
            for cookie in cookies
            if cookie["domain"].lstrip(".") in BASE_URL
        ]

        cloudflare_cookies = cloudflare_cookies[0]

        # pegando a pagina do player e extraindo x-page-token

        player_area_headers = headers["player_area"]
        player_area_headers["referer"] = player_area_headers["referer"].replace("@ID", player.movie_id)

        cookies = cloudflare_cookies.copy()
        response = requests.get(f'{BASE_URL}/filme/{player.movie_id}', cookies=cookies, headers=player_area_headers)

        match = re.search(
        r'''var\s+PAGE_TOKEN\s*=\s*["']([^"']+)["']''',
            response.text
        )


        if not match:
            raise ValueError("[PLAYERFLIX MOVIE PREMIUM] ERRO AO PUXAR PAGE TOKEN")

        PAGE_TOKEN = match.group(1)

        # extraindo INITIAL_CONTENT_ID

        match = re.search(r'INITIAL_CONTENT_ID\s*=\s*(\d+)', response.text)

        if not match or int(match.group(1)) == 0:
            raise ValueError("[PLAYERFLIX MOVIE PREMIUM] ERRO INITIAL_CONTENT_ID NÃO ENCONTRADO")
        
        INITIAL_CONTENT_ID = int(match.group(1))

        # obtendo video_id

        headers_boostrap_script = headers["boostrap_script"]
        headers_boostrap_script["referer"] = headers_boostrap_script["referer"].replace("@ID", player.movie_id)
        headers_boostrap_script["x-page-token"] = PAGE_TOKEN

        cookies = cloudflare_cookies.copy()

        data = {
            'contentid': INITIAL_CONTENT_ID,
            'type': 'filme',
            'season': '1',
            'episode': '',
            '_token': '',
            'page_token': PAGE_TOKEN,
            'pageToken': PAGE_TOKEN,
        }

        response = requests.post(f'{BASE_URL}/player/bootstrap', cookies=cookies, headers=headers_boostrap_script, data=data)
        response_obj = response.json()

        if response_obj.get("data", None):
            VIDEO_ID = str(response_obj["data"]["options"][0]["ID"])

            if not VIDEO_ID.isnumeric():

                raise ValueError("[PLAYERFLIX MOVIE PREMIUM] REDECANAIS INDETIFICADO (SEM VIDEO URL)")

        else:
            raise ValueError("[PLAYERFLIX MOVIE PREMIUM] ERRO VIDEO_ID NÃO ENCONTRADO")

        # obtendo pagina do video 

        headers_video_page = headers["boostrap_script"]
        headers_video_page["referer"] = headers_video_page["referer"].replace("@ID", player.movie_id)
        headers_video_page["x-page-token"] = PAGE_TOKEN
        
        data = {
            'video_id': VIDEO_ID,
            'page_token': PAGE_TOKEN,
            'host': '',
            'site': '',
            '_token': '',
        }

        cookies = cloudflare_cookies.copy()
        response = requests.post(f"{BASE_URL}/player/source", cookies=cookies, headers=headers_video_page, data=data)
        response_obj = response.json()

        if response_obj.get("data", None):
            PLAYER_URL = response_obj["data"]["video_url"]

        else:
            raise ValueError(f"[PLAYERFLIX MOVIE PREMIUM] ERRO AO PUXAR URL DO PLAYER ")

        # obtendo o cookie fireplayer_player

        headers_player_final = headers["player_final"]
        headers_player_final["referer"] = headers_player_final["referer"].replace("@ID", player.movie_id)

        response = requests.get(PLAYER_URL, cookies=cookies, headers=headers_player_final, allow_redirects=True)

        if "fireplayer_player" in response.cookies.keys():
            FIREPLAYER_COOKIES = response.cookies

        else:
            raise ValueError("[PLAYERFLIX MOVIE PREMIUM] COOKIE FIREPLAYER_PLAYER  NÃO ENCONTRADO ENCONTRADO")

        # obtendo PLAYER_BASE_URL


        match = re.search(r'player_base_url\s*=\s*["\']([^"\']+)["\']', response.text)

        if match:
            PLAYER_BASE_URL = match.group(1)

        else:
            raise ValueError("[PLAYERFLIX MOVIE PREMIUM] PLAYER_BASE_URL NÃO ENCONTRADO")


        # obtendo o VIDEO_ID


        params = {
            'v': '7',
        }

        headers_player_script = headers["player_script"]
        headers_player_script["referer"] = headers_player_script["referer"].replace("@REFERER", response.url)


        cookies = cloudflare_cookies.copy()
        cookies.update(FIREPLAYER_COOKIES)

        response = requests.get(
            f'{PLAYER_BASE_URL}/player/assets/scripts.php',
            params=params,
            cookies=cookies,
            headers=headers_player_script,
        )

        match = re.search(
            r'sfServerReferrer\s*=\s*["\'][^"\']*/video/([a-fA-F0-9]+)["\']',
            response.text
        )

        if match:
            VIDEO_ID = match.group(1)
        else:
            raise ValueError("[PLAYERFLIX MOVIE PREMIUM] VIDEO_ID NÃO ENCONTRADO")

        # obtendo o VIDEO_HASH

        match = re.search(
            r'url\s*:\s*["\'][^"\']*/layer/([^/"\']+)/["\']\s*\+\s*ID',
            response.text
        )

        if match:
            VIDEO_HASH = match.group(1)
        else:
            raise ValueError("[PLAYERFLIX MOVIE PREMIUM] VIDEO_HASH NÃO ENCONTRADO")


        # obtendo playlist m3u8

        headers_video_data = headers["video_data"]
        headers_video_data["origin"] = PLAYER_BASE_URL
        headers_video_data["referer"] = f"{PLAYER_BASE_URL}/video/{VIDEO_ID}"


        cookies = cloudflare_cookies.copy()
        cookies.update(FIREPLAYER_COOKIES)

        data = {
            'hash': VIDEO_ID,
            'r': 'https://superflixapi.fun/',
        }

        response = requests.post(
            f'{PLAYER_BASE_URL}/layer/{VIDEO_HASH}/{VIDEO_ID}/',
            cookies=cookies,
            headers=headers_video_data,
            data=data,
        )

        return VideoSource(**response.json())

    async def _extract( self, player: "Player", headers: dict) -> str:
        browser = Browser(player, headers)
        cookies = []
        try:
            cookies = await browser.get_cookies()

        except Exception as e:
            raise ValueError("[PLAYERFLIX MOVIE PREMIUM] ERRO AO PEGAR COOKIES DE CLOUDFLARE EM FILMES")
        finally:
            await browser.close()
        
        return cookies