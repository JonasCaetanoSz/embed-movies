from dataclasses import dataclass
from ..extractors import premium_player_extractor

EXTRACTORS = {
    "Premium": premium_player_extractor.PremiumPlayerExtractor(),
}


@dataclass
class Player:
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


    def get_stream(self):
        extractor = EXTRACTORS.get(self.label)
        return extractor.extract(self)