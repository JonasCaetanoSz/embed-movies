from dataclasses import dataclass


@dataclass
class Player:
    id: str
    title: str
    release_date: str
    label: str
    embed: str
    lang: str
    budget: str
    icon: str
    embed_id: str | None = None