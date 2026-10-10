import os

import requests

from .models.episode_details import EpisodeDetails


class DetailsEpisode:
    BASE_URL = "https://api.themoviedb.org/3"

    def __init__(
        self,
        token: str | None = None,
        session: requests.Session | None = None,
    ):
        self.token = token or os.getenv("TMDB_TOKEN")

        if not self.token:
            raise ValueError("TMDB API token not provided.")

        self.session = session or requests.Session()

    def get_episode(
        self,
        tv_id: int,
        season_number: int,
        episode_number: int,
        language: str = "pt-BR",
    ) -> EpisodeDetails:
        if isinstance(tv_id, bool) or not isinstance(tv_id, int) or tv_id <= 0:
            raise ValueError("tv_id must be a positive integer.")

        if (
            isinstance(season_number, bool)
            or not isinstance(season_number, int)
            or season_number < 0
        ):
            raise ValueError("season_number must be a non-negative integer.")

        if (
            isinstance(episode_number, bool)
            or not isinstance(episode_number, int)
            or episode_number <= 0
        ):
            raise ValueError("episode_number must be a positive integer.")

        if not isinstance(language, str) or not language.strip():
            raise ValueError("language must be a non-empty string.")

        url = (
            f"{self.BASE_URL}/tv/{tv_id}/season/"
            f"{season_number}/episode/{episode_number}"
        )

        response = self.session.get(
            url,
            params={
                "api_key": self.token,
                "language": language,
                "append_to_response": "credits,images,videos,external_ids",
            },
            timeout=10,
        )

        response.raise_for_status()
        data = response.json()

        return EpisodeDetails(
            id=data["id"],
            name=data.get("name", ""),
            overview=data.get("overview", ""),
            episode_number=data["episode_number"],
            season_number=data["season_number"],
            air_date=data.get("air_date"),
            still_path=data.get("still_path"),
            vote_average=data.get("vote_average", 0.0),
            vote_count=data.get("vote_count", 0),
            runtime=data.get("runtime"),
            production_code=data.get("production_code"),
            episode_type=data.get("episode_type"),
            show_id=data.get("show_id"),
            crew=tuple(data.get("crew") or ()),
            guest_stars=tuple(data.get("guest_stars") or ()),
            credits=data.get("credits"),
            images=data.get("images"),
            videos=data.get("videos"),
            external_ids=data.get("external_ids"),
        )