from dataclasses import dataclass
from typing import Optional


from ..extractors import (
    watch_play_player_extractor,
    premium_player_extractor,
    vip_player_player_extractor,
    base
)

EXTRACTORS = {
    "WatchPlay": watch_play_player_extractor.WatchPlayPlayerExtractor(),
    "Premium": premium_player_extractor.PremiumPlayerExtractor(),
    "VIP Player": vip_player_player_extractor.VipPlayerPlayerExtractor()
}

@dataclass
class TvPlayer:
    id: str
    title: str
    release_date: str
    label: str
    embed: str
    lang: str
    budget: str
    icon: str
    embed_id: Optional[str] = None
    tv_id: Optional[int] = None
    episode: Optional[str]  = None
    season: Optional[str] = None

    def get_stream(self) -> base.TvVideoSource:
        extractor = EXTRACTORS.get(self.label)
        return extractor.extract(self)