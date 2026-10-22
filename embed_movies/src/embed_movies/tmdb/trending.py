
import os

import requests


class Trending:
    BASE_URL = "https://api.themoviedb.org/3"

    def __init__(
        self,
        token: str | None = None,
        session: requests.Session | None = None,
    ):
        self.token = token or os.getenv("TMDB_TOKEN")

        if not self.token:
            raise ValueError("Token da API do TMDB não informado.")

        self.session = session or requests.Session()

    def trending(
        self,
        media_type: str = "all",
        time_window: str = "day",
        language: str = "pt-BR",
        page: int = 1,
    ) -> list[dict]:
        """Retorna os conteúdos em alta no TMDB."""

        if media_type not in ("all", "movie", "tv"):
            raise ValueError(
                "O tipo de mídia deve ser 'all', 'movie' ou 'tv'."
            )

        if time_window not in ("day", "week"):
            raise ValueError(
                "O período deve ser 'day' ou 'week'."
            )

        if (
            isinstance(page, bool)
            or not isinstance(page, int)
            or page < 1
        ):
            raise ValueError(
                "O número da página deve ser um inteiro positivo."
            )

        if not isinstance(language, str) or not language.strip():
            raise ValueError(
                "O idioma deve ser uma string não vazia."
            )

        url = (
            f"{self.BASE_URL}/trending/"
            f"{media_type}/{time_window}"
        )

        response = self.session.get(
            url,
            params={
                "api_key": self.token,
                "language": language,
                "page": page,
            },
            timeout=10,
        )

        response.raise_for_status()

        data = response.json()
        return data.get("results", [])

    def movies(
        self,
        time_window: str = "day",
        language: str = "pt-BR",
        page: int = 1,
    ) -> list[dict]:
        """Retorna os filmes em alta."""
        return self.trending(
            media_type="movie",
            time_window=time_window,
            language=language,
            page=page,
        )

    def tv_shows(
        self,
        time_window: str = "day",
        language: str = "pt-BR",
        page: int = 1,
    ) -> list[dict]:
        """Retorna as séries em alta."""
        return self.trending(
            media_type="tv",
            time_window=time_window,
            language=language,
            page=page,
        )

    def all(
        self,
        time_window: str = "day",
        language: str = "pt-BR",
        page: int = 1,
    ) -> list[dict]:
        """Retorna todos os conteúdos em alta."""
        return self.trending(
            media_type="all",
            time_window=time_window,
            language=language,
            page=page,
        )
