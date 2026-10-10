import os

import requests

from .models import TVDetails


class DetailsTV:
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

    def get_tv(
        self,
        tv_id: int,
        language: str = "pt-BR",
    ) -> TVDetails:
        if not isinstance(tv_id, int) or isinstance(tv_id, bool):
            raise TypeError("O ID da série deve ser um inteiro.")

        if tv_id < 1:
            raise ValueError(
                "O ID da série deve ser maior que zero."
            )

        response = self.session.get(
            f"{self.BASE_URL}/tv/{tv_id}",
            params={
                "api_key": self.token,
                "language": language,
            },
            timeout=15,
        )

        response.raise_for_status()

        return TVDetails.from_dict(response.json())