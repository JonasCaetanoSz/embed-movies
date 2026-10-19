from pathlib import Path

import requests

import json

from ..models import ResponseDataPlayers, Player

class MovieAvailablePlayers:
    @staticmethod
    def get_available_players_movie(movie_id: str) -> list[Player]:

        headers_path = Path(__file__).parent / "players_available_headers.json"
        url_base = "https://playerflix.ink/inc/Ajax.php"

        with open(file=headers_path, mode="r", encoding="utf-8") as f:
            headers = json.load(fp=f)["get_players"]

        headers["referer"] = headers["referer"].replace("@ID", movie_id)

        params = {
            'type': 'movie',
            'id': str(movie_id),
            'season': 'null',
            'episode': 'null',
        }

        response = requests.get(url=url_base, params=params, headers=headers)
        response.raise_for_status()

        response_json = response.json()
        response_json["data"]["movie_id"] = movie_id

        response_obj = ResponseDataPlayers(**response_json)
        return response_obj.players