from dataclasses import dataclass


@dataclass
class VideoSource:
    hls: bool
    videoImage: str
    videoSource: str
    securedLink: str
    downloadLinks: list[str]
    attachmentLinks: list[str]
    ck: str