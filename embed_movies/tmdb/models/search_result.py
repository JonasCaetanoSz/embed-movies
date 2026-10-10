from dataclasses import dataclass, field
from typing import Any

from .movie import Movie
from .tv_show import TVShow


@dataclass
class SearchResult:
    movies: list[Movie] = field(default_factory=list)
    tv_shows: list[TVShow] = field(default_factory=list)
    people: list[dict[str, Any]] = field(default_factory=list)

    @classmethod
    def from_list(cls, results: list[dict]) -> "SearchResult":
        result = cls()

        for item in results:
            media_type = item.get("media_type")

            if media_type == "movie" or (
                media_type is None and "title" in item
            ):
                result.movies.append(Movie.from_dict(item))

            elif media_type == "tv" or (
                media_type is None and "name" in item
            ):
                result.tv_shows.append(TVShow.from_dict(item))

            elif media_type == "person":
                result.people.append(item)

        return result