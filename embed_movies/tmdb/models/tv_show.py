from dataclasses import dataclass, field


@dataclass
class TVShow:
    id: int
    name: str
    overview: str = ""
    poster_path: str | None = None
    backdrop_path: str | None = None
    first_air_date: str | None = None
    vote_average: float = 0.0
    vote_count: int = 0
    popularity: float = 0.0
    original_language: str | None = None
    genre_ids: list[int] = field(default_factory=list)

    @classmethod
    def from_dict(cls, data: dict) -> "TVShow":
        return cls(
            id=data["id"],
            name=data.get("name", ""),
            overview=data.get("overview", ""),
            poster_path=data.get("poster_path"),
            backdrop_path=data.get("backdrop_path"),
            first_air_date=data.get("first_air_date"),
            vote_average=data.get("vote_average", 0.0),
            vote_count=data.get("vote_count", 0),
            popularity=data.get("popularity", 0.0),
            original_language=data.get("original_language"),
            genre_ids=data.get("genre_ids", []),
        )