from pathlib import Path

import requests

import json

from ..models import ResponseDataPlayersTv, TvPlayer

class TvAvailablePlayers:
    @staticmethod
    def get_available_players_tv(tv_id: str, season:str, episode:str) -> list[TvPlayer]:

        headers_path = Path(__file__).parent / "players_available_headers.json"
        url_base = "https://playerflix.ink/inc/Ajax.php"

        with open(file=headers_path, mode="r", encoding="utf-8") as f:
            headers = json.load(fp=f)["get_players"]

        headers["referer"] = headers["referer"].replace("@PATH", f"{season}/{episode}")

        params = {
            'type': 'tv',
            'id': str(tv_id),
            'season': str(season),
            'episode': str(episode),
        }

        response = requests.get(url=url_base, params=params, headers=headers)
        response.raise_for_status()

        response_json = response.json()
        response_json["data"]["tv_id"] = str(tv_id)

        response_obj = ResponseDataPlayersTv(**response_json)
        return response_obj.players