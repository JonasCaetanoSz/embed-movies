from dataclasses import dataclass, field


@dataclass(frozen=True, slots=True)
class EpisodeDetails:
    id: int
    name: str
    overview: str
    episode_number: int
    season_number: int

    air_date: str | None = None
    still_path: str | None = None
    vote_average: float = 0.0
    vote_count: int = 0
    runtime: int | None = None
    production_code: str | None = None
    episode_type: str | None = None
    show_id: int | None = None

    crew: tuple[dict, ...] = field(default_factory=tuple)
    guest_stars: tuple[dict, ...] = field(default_factory=tuple)

    credits: dict | None = None
    images: dict | None = None
    videos: dict | None = None
    external_ids: dict | None = None

    @property
    def still_url(self) -> str | None:
        if not self.still_path:
            return None

        return f"https://image.tmdb.org/t/p/original{self.still_path}"

    @property
    def still_url_w500(self) -> str | None:
        if not self.still_path:
            return None

        return f"https://image.tmdb.org/t/p/w500{self.still_path}"