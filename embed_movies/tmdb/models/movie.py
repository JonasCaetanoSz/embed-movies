from dataclasses import dataclass, field


@dataclass
class Movie:
    id: int
    title: str
    overview: str = ""
    poster_path: str | None = None
    backdrop_path: str | None = None
    release_date: str | None = None
    vote_average: float = 0.0
    vote_count: int = 0
    popularity: float = 0.0
    original_language: str | None = None
    genre_ids: list[int] = field(default_factory=list)

    @classmethod
    def from_dict(cls, data: dict) -> "Movie":
        return cls(
            id=data["id"],
            title=data.get("title", ""),
            overview=data.get("overview", ""),
            poster_path=data.get("poster_path"),
            backdrop_path=data.get("backdrop_path"),
            release_date=data.get("release_date"),
            vote_average=data.get("vote_average", 0.0),
            vote_count=data.get("vote_count", 0),
            popularity=data.get("popularity", 0.0),
            original_language=data.get("original_language"),
            genre_ids=data.get("genre_ids", []),
        )