import asyncio
import sys
from pathlib import Path
from unittest.mock import Mock

import pytest

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "embed_movies" / "src"
sys.path.insert(0, str(SRC))

from streaming.clients.playerflix.movie.extractors import (
    premium_player_extractor,
)
from streaming.clients.playerflix.movie.models import VideoSource


class CompletedLoop:
    def run_until_complete(self, coroutine):
        return asyncio.run(coroutine)


@pytest.fixture
def player():
    return Mock(movie_id="movie-123", embed="https://example.com/embed")


@pytest.fixture
def extractor(monkeypatch):
    headers = {
        "player_area": {"referer": "https://superflixapi.fun/filme/@ID"},
        "boostrap_script": {"referer": "https://superflixapi.fun/@ID"},
        "player_final": {"referer": "https://superflixapi.fun/@ID"},
        "player_script": {"referer": "@REFERER"},
        "video_data": {},
    }
    monkeypatch.setattr(
        premium_player_extractor.json,
        "load",
        lambda _: headers,
    )
    monkeypatch.setattr(
        premium_player_extractor.uc,
        "loop",
        lambda: CompletedLoop(),
    )

    instance = premium_player_extractor.PremiumPlayerExtractor()

    async def get_cookies(player, headers):
        return [
            {
                "name": "cf_clearance",
                "value": "clearance-token",
                "domain": ".superflixapi.fun",
            }
        ]

    monkeypatch.setattr(instance, "_extract", get_cookies)
    return instance


def response(*, text="", data=None, cookies=None, url=""):
    result = Mock()
    result.text = text
    result.url = url
    result.cookies = cookies or {}
    result.json.return_value = data
    return result


def test_extract_returns_video_source(monkeypatch, extractor, player):
    player_page = response(
        text='var PAGE_TOKEN = "page-token"; INITIAL_CONTENT_ID = 321;'
    )
    bootstrap = response(data={"data": {"options": [{"ID": "98765"}]}})
    source = response(
        data={"data": {"video_url": "https://player.example/embed"}}
    )
    player_page_final = response(
        text='var player_base_url = "https://player.example";',
        cookies={"fireplayer_player": "player-cookie"},
        url="https://player.example/landing",
    )
    player_script = response(
        text=(
            'sfServerReferrer = "https://player.example/video/AB12"; '
            'url: "https://player.example/layer/hash-value/" + ID'
        )
    )
    video_data = {
        "hls": True,
        "videoImage": "https://cdn.example/poster.jpg",
        "videoSource": "https://cdn.example/video.m3u8",
        "securedLink": "https://cdn.example/secured",
        "downloadLinks": ["https://cdn.example/download"],
        "attachmentLinks": [],
        "ck": "signature",
    }
    playlist = response(data=video_data)

    mock_get = Mock(
        side_effect=[player_page, player_page_final, player_script]
    )
    mock_post = Mock(side_effect=[bootstrap, source, playlist])
    monkeypatch.setattr(premium_player_extractor.requests, "get", mock_get)
    monkeypatch.setattr(premium_player_extractor.requests, "post", mock_post)

    result = extractor.extract(player)

    assert isinstance(result, VideoSource)
    assert result.videoSource == "https://cdn.example/video.m3u8"
    assert result.videoImage == "https://cdn.example/poster.jpg"
    assert result.downloadLinks == ["https://cdn.example/download"]

    assert mock_get.call_count == 3
    assert mock_post.call_count == 3
    assert mock_post.call_args_list[0].kwargs["data"]["contentid"] == 321
    assert mock_post.call_args_list[1].kwargs["data"]["video_id"] == "98765"
    assert mock_post.call_args_list[2].kwargs["data"] == {
        "hash": "AB12",
        "r": "https://superflixapi.fun/",
    }


def test_extract_returns_none_when_no_cookies(monkeypatch, extractor, player):
    async def no_cookies(player, headers):
        return []

    monkeypatch.setattr(extractor, "_extract", no_cookies)
    mock_get = Mock()
    mock_post = Mock()
    monkeypatch.setattr(premium_player_extractor.requests, "get", mock_get)
    monkeypatch.setattr(premium_player_extractor.requests, "post", mock_post)

    assert extractor.extract(player) is None
    mock_get.assert_not_called()
    mock_post.assert_not_called()


def test_extract_raises_when_page_token_is_missing(
    monkeypatch,
    extractor,
    player,
):
    mock_get = Mock(return_value=response(text="INITIAL_CONTENT_ID = 321;"))
    mock_post = Mock()
    monkeypatch.setattr(premium_player_extractor.requests, "get", mock_get)
    monkeypatch.setattr(premium_player_extractor.requests, "post", mock_post)

    with pytest.raises(ValueError, match="ERRO AO PUXAR PAGE TOKEN"):
        extractor.extract(player)

    mock_get.assert_called_once()
    mock_post.assert_not_called()
