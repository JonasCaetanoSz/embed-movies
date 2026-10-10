import os

import requests

from .models import SeasonDetails


class DetailsSeason:
    BASE_URL = "https://api.themoviedb.org/3"

    def __init__(
        self,
        token: str | None = None,
        session: requests.Session | None = None,
    ):
        self.token = token or os.getenv("TMDB_TOKEN")

        if not self.token:
            raise ValueError("Token do TMDB não configurado.")

        self.session = session or requests.Session()

    def get_season(
        self,
        tv_id: int,
        season_number: int,
        language: str = "pt-BR",
    ) -> SeasonDetails:
        if not isinstance(tv_id, int) or isinstance(tv_id, bool):
            raise TypeError("O ID da série deve ser um inteiro.")

        if tv_id < 1:
            raise ValueError("O ID da série deve ser maior que zero.")

        if (
            not isinstance(season_number, int)
            or isinstance(season_number, bool)
        ):
            raise TypeError("O número da temporada deve ser um inteiro.")

        if season_number < 0:
            raise ValueError(
                "O número da temporada não pode ser negativo."
            )

        response = self.session.get(
            f"{self.BASE_URL}/tv/{tv_id}/season/{season_number}",
            params={
                "api_key": self.token,
                "language": language,
            },
            timeout=15,
        )

        response.raise_for_status()

        return SeasonDetails.from_dict(response.json())