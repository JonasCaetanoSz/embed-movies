
from dataclasses import dataclass
from typing import Optional


@dataclass
class MovieDetails:
    id: int
    title: str
    overview: str = ""
    release_date: Optional[str] = None
    runtime: Optional[int] = None
    status: Optional[str] = None
    tagline: Optional[str] = None
    vote_average: float = 0.0
    vote_count: int = 0
    poster_path: Optional[str] = None
    backdrop_path: Optional[str] = None
    genres: tuple[str, ...] = ()
    original_language: Optional[str] = None
    original_title: Optional[str] = None
    budget: int = 0
    revenue: int = 0
    homepage: Optional[str] = None

    @classmethod
    def from_dict(cls, data: dict) -> "MovieDetails":
        return cls(
            id=data["id"],
            title=data.get("title", ""),
            overview=data.get("overview", ""),
            release_date=data.get("release_date"),
            runtime=data.get("runtime"),
            status=data.get("status"),
            tagline=data.get("tagline"),
            vote_average=data.get("vote_average", 0.0),
            vote_count=data.get("vote_count", 0),
            poster_path=data.get("poster_path"),
            backdrop_path=data.get("backdrop_path"),
            genres=tuple(
                genre["name"]
                for genre in data.get("genres", [])
            ),
            original_language=data.get("original_language"),
            original_title=data.get("original_title"),
            budget=data.get("budget", 0),
            revenue=data.get("revenue", 0),
            homepage=data.get("homepage"),
        )

    @property
    def poster_url(self) -> Optional[str]:
        if not self.poster_path:
            return None

        return (
            "https://image.tmdb.org/t/p/w500"
            f"{self.poster_path}"
        )

    @property
    def backdrop_url(self) -> Optional[str]:
        if not self.backdrop_path:
            return None

        return (
            "https://image.tmdb.org/t/p/original"
            f"{self.backdrop_path}"
        )
