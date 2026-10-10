
import os

import requests

from .models import MovieDetails


class DetailsMovie:
    BASE_URL = "https://api.themoviedb.org/3"

    def __init__(
        self,
        token: str | None = None,
        session: requests.Session | None = None,
    ):
        self.token = token or os.getenv("TMDB_TOKEN")

        if not self.token:
            raise ValueError(
                "Token do TMDB não configurado."
            )

        self.session = session or requests.Session()

    def get_movie(
        self,
        movie_id: int,
        language: str = "pt-BR",
    ) -> MovieDetails:
        if not isinstance(movie_id, int) or isinstance(movie_id, bool):
            raise TypeError("O ID do filme deve ser um inteiro.")

        if movie_id < 1:
            raise ValueError(
                "O ID do filme deve ser maior que zero."
            )

        response = self.session.get(
            f"{self.BASE_URL}/movie/{movie_id}",
            params={
                "api_key": self.token,
                "language": language,
            },
            timeout=15,
        )

        response.raise_for_status()

        return MovieDetails.from_dict(response.json())
