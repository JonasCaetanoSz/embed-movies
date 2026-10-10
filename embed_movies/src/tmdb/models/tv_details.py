from dataclasses import dataclass
from typing import Optional


@dataclass
class TVDetails:
    id: int
    name: str
    overview: str = ""
    first_air_date: Optional[str] = None
    last_air_date: Optional[str] = None
    status: Optional[str] = None
    tagline: Optional[str] = None
    vote_average: float = 0.0
    vote_count: int = 0
    poster_path: Optional[str] = None
    backdrop_path: Optional[str] = None
    number_of_seasons: int = 0
    number_of_episodes: int = 0
    episode_run_time: tuple[int, ...] = ()
    genres: tuple[str, ...] = ()
    original_language: Optional[str] = None
    original_name: Optional[str] = None
    homepage: Optional[str] = None

    @classmethod
    def from_dict(cls, data: dict) -> "TVDetails":
        return cls(
            id=data["id"],
            name=data.get("name", ""),
            overview=data.get("overview", ""),
            first_air_date=data.get("first_air_date"),
            last_air_date=data.get("last_air_date"),
            status=data.get("status"),
            tagline=data.get("tagline"),
            vote_average=data.get("vote_average", 0.0),
            vote_count=data.get("vote_count", 0),
            poster_path=data.get("poster_path"),
            backdrop_path=data.get("backdrop_path"),
            number_of_seasons=data.get("number_of_seasons", 0),
            number_of_episodes=data.get("number_of_episodes", 0),
            episode_run_time=tuple(data.get("episode_run_time", [])),
            genres=tuple(
                genre["name"]
                for genre in data.get("genres", [])
            ),
            original_language=data.get("original_language"),
            original_name=data.get("original_name"),
            homepage=data.get("homepage"),
        )

    @property
    def poster_url(self) -> Optional[str]:
        if not self.poster_path:
            return None

        return f"https://image.tmdb.org/t/p/w500{self.poster_path}"

    @property
    def backdrop_url(self) -> Optional[str]:
        if not self.backdrop_path:
            return None

        return (
            f"https://image.tmdb.org/t/p/original"
            f"{self.backdrop_path}"
        )