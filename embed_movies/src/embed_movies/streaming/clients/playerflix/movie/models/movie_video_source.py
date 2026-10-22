from dataclasses import dataclass


@dataclass
class MovieVideoSource:
    hls: bool
    videoImage: str
    videoSource: str
    securedLink: str
    downloadLinks: list[str]
    attachmentLinks: list[str]
    ck: str