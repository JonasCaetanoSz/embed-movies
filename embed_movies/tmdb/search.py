import os
import requests

from embed_movies.tmdb.models import Movie, TVShow, SearchResult


class Search:
    BASE_URL = "https://api.themoviedb.org/3"

    def __init__(self, token: str | None = None):
        self.token = token or os.getenv("TMDB_TOKEN")

        if not self.token:
            raise ValueError("Token do TMDB não configurado.")

        self.session = requests.Session()

    def search(self, query: str,media_type: str = "multi",page: int = 1, language: str = "pt-BR") -> list[Movie] | list[TVShow] | SearchResult:

        if not query.strip():
            raise ValueError("A busca não pode estar vazia.")

        if media_type not in ("movie", "tv", "multi"):
            raise ValueError(
                "media_type deve ser 'movie', 'tv' ou 'multi'."
            )

        if page < 1:
            raise ValueError("A página deve ser maior que zero.")

        response = self.session.get(
            f"{self.BASE_URL}/search/{media_type}",
            params={
                "api_key": self.token,
                "query": query,
                "page": page,
                "language": language,
            },
            timeout=15,
        )

        response.raise_for_status()

        data = response.json()
        results = data.get("results", [])

        if media_type == "movie":
            return [
                Movie.from_dict(item)
                for item in results
            ]

        if media_type == "tv":
            return [
                TVShow.from_dict(item)
                for item in results
            ]

        return SearchResult.from_list(results)