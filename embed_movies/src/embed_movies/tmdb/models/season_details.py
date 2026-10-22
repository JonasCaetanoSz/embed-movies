from dataclasses import dataclass
from typing import Optional


@dataclass
class SeasonEpisode:
    id: int
    name: str
    episode_number: int
    season_number: int
    overview: str = ""
    air_date: Optional[str] = None
    vote_average: float = 0.0
    still_path: Optional[str] = None

    @classmethod
    def from_dict(cls, data: dict) -> "SeasonEpisode":
        return cls(
            id=data["id"],
            name=data.get("name", ""),
            episode_number=data.get("episode_number", 0),
            season_number=data.get("season_number", 0),
            overview=data.get("overview", ""),
            air_date=data.get("air_date"),
            vote_average=data.get("vote_average", 0.0),
            still_path=data.get("still_path"),
        )

    @property
    def still_url(self) -> Optional[str]:
        if not self.still_path:
            return None

        return (
            f"https://image.tmdb.org/t/p/w500"
            f"{self.still_path}"
        )


@dataclass
class SeasonDetails:
    id: int
    name: str
    season_number: int
    overview: str = ""
    air_date: Optional[str] = None
    poster_path: Optional[str] = None
    vote_average: float = 0.0
    episodes: tuple[SeasonEpisode, ...] = ()

    @classmethod
    def from_dict(cls, data: dict) -> "SeasonDetails":
        episodes = tuple(
            SeasonEpisode.from_dict(episode)
            for episode in data.get("episodes", [])
        )

        return cls(
            id=data["id"],
            name=data.get("name", ""),
            season_number=data.get("season_number", 0),
            overview=data.get("overview", ""),
            air_date=data.get("air_date"),
            poster_path=data.get("poster_path"),
            vote_average=data.get("vote_average", 0.0),
            episodes=episodes,
        )

    @property
    def episode_count(self) -> int:
        return len(self.episodes)

    @property
    def poster_url(self) -> Optional[str]:
        if not self.poster_path:
            return None

        return (
            f"https://image.tmdb.org/t/p/w500"
            f"{self.poster_path}"
        )