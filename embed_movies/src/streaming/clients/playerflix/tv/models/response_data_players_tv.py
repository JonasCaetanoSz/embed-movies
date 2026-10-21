from dataclasses import dataclass

from .player_tv import TvPlayer


@dataclass
class ResponseDataPlayersTv:
    status: bool
    data: dict

    @property
    def players(self) -> list[TvPlayer]:
        series = self.data

        return [
            TvPlayer(
                id=item["id"],
                title=series["title"].get("pt", ""),
                release_date=series["release_date"],
                label=item["label"],
                embed=item["embed"],
                lang=item["lang"],
                budget=item["budget"],
                icon=item["icon"],
                embed_id=item.get("embed_id"),
                tv_id=series.get("tv_id"),
            )
            for item in series["options"]
        ]