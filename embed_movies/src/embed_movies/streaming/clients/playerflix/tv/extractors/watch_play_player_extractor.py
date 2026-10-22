from pathlib import Path

from typing import TYPE_CHECKING

import requests

from .base import BasePlayerExtractor

import json

import re

from..models.tv_video_source import TvVideoSource

if TYPE_CHECKING:
    from ..models import TvPlayer
    
class WatchPlayPlayerExtractor(BasePlayerExtractor):

    def extract(self, player: "TvPlayer"):

        headers_path = Path(__file__).parent / "watch_play_player_headers.json"

        with open(headers_path, "r", encoding="utf-8") as file:
            headers = json.load(file)

        player_page_headers = headers["player_page"]
        response = requests.get(url=player.embed, headers=player_page_headers)

        match = re.search(
            r'window\.MyPlayerAudio="Dublado".*?url:"(https?:\\?/\\?/.*?)"',
            response.text
        )

        if not match:
            raise ValueError("[PLAYERFLIX TV WATCHPLAY] ERRO AO EXTRAIR VIDEO URL")
        
        VIDEO_URL = match.group(1).replace(r'\/', '/')
        
        return TvVideoSource(
            hls=None,
            videoImage=None,
            videoSource=VIDEO_URL,
            securedLink=None,
            downloadLinks=[],
            attachmentLinks=[],
            ck=None)