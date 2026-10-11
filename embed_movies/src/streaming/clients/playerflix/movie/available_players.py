from pathlib import Path

import requests

import json

from .models import ResponseDataPlayers, Player

class MovieAvailablePlayers:
    @staticmethod
    def get_available_players_movie(movie_id: str) -> list[Player]:

        headers_path = Path(__file__).parent / "headers.json"
        url_base = "https://playerflix.ink/inc/Ajax.php"
        headers = None

        with open(file=headers_path, mode="r", encoding="utf-8") as f:
            headers = json.load(fp=f)["get_players"]

        params = {
            'type': 'movie',
            'id': str(movie_id),
            'season': 'null',
            'episode': 'null',
        }

        response = requests.get(url=url_base, params=params, headers=headers)
        response.raise_for_status()

        response_obj = ResponseDataPlayers(**response.json())
        return response_obj.players