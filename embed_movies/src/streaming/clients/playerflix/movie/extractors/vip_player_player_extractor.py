from pathlib import Path

from typing import TYPE_CHECKING

from ..models.video_source import VideoSource

import requests

from .base import BasePlayerExtractor

from urllib.parse import urlparse
import json

import re

if TYPE_CHECKING:
    from ..models import Player
    
class VipPlayerPlayerExtractor(BasePlayerExtractor):

    def extract(self, player: "Player") -> VideoSource:

        headers_path = Path(__file__).parent / "vip_player_player_headers.json"

        with open(headers_path, "r", encoding="utf-8") as file:
            headers = json.load(file)

        match = re.search(r"/video/([a-f0-9]+)", player.embed)

        VIDEO_HASH = match.group(1) if match else None

        if not VIDEO_HASH:
            raise ValueError("[PLAYERFLIX MOVIE VIP PLAYER] ERRO AO EXTRAIR VIDEO HASH")


        PLAYER_BASE_URL = "https://" + urlparse(player.embed).netloc

        get_player_headers = headers["get_player"]

        params = {
            'data': VIDEO_HASH,
            'do': 'getVideo',
        }

        data = {
            'hash': VIDEO_HASH,
            'r': '',
        }

        response = requests.post(f'{PLAYER_BASE_URL}/player/index.php', params=params, headers=get_player_headers, data=data)
        return VideoSource(**response.json())