from dataclasses import dataclass

from ..extractors import (
    premium_player_extractor,
    watch_play_player_extractor,
    vip_player_player_extractor,
    base
)

EXTRACTORS = {
    "Premium": premium_player_extractor.PremiumPlayerExtractor(),
    "WatchPlay": watch_play_player_extractor.WatchPlayPlayerExtractor(),
    "VIP Player": vip_player_player_extractor.VipPlayerPlayerExtractor()
}


@dataclass
class Movieplayer:
    id: str
    title: str
    movie_id: str
    release_date: str
    label: str
    embed: str
    lang: str
    budget: str
    icon: str
    embed_id: str | None = None


    def get_stream(self) -> base.MovieVideoSource:
        extractor = EXTRACTORS.get(self.label)
        if not extractor:
            raise NotImplementedError(f"[PLAYERFLIX MOVIE] nenhuma extrator disponivel para o player : {self.label}")
        
        return extractor.extract(self)