from dataclasses import dataclass

from .player import Player


@dataclass
class ResponseDataPlayers:
    status: bool
    data: dict

    @property
    def players(self) -> list[Player]:
        movie = self.data

        return [
            Player(
                id=item["id"],
                title=movie["title"].get("pt", ""),
                release_date=movie["release_date"],
                label=item["label"],
                embed=item["embed"],
                lang=item["lang"],
                budget=item["budget"],
                icon=item["icon"],
                embed_id=item.get("embed_id"),
                movie_id=movie.get("movie_id")
            )
            for item in movie["options"]
        ]