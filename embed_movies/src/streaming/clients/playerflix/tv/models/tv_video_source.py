from dataclasses import dataclass


@dataclass
class TvVideoSource:
    hls: bool
    videoImage: str | None
    videoSource: str
    securedLink: str
    downloadLinks: list[str]
    attachmentLinks: list[str]
    ck: str