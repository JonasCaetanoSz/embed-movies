import json
from unittest.mock import Mock

import pytest

from embed_movies.streaming.clients.playerflix.tv.extractors import (
    watch_play_player_extractor,
)
from embed_movies.streaming.clients.playerflix.tv.models import TvVideoSource


@pytest.fixture
def player():
    return Mock(
        tv_id="1396",
        embed="https://example.com/episode-player",
    )


@pytest.fixture
def extractor(monkeypatch):
    headers = {
        "player_page": {
            "referer": "https://playerflix.ink/serie/1396/2/3",
        },
    }
    monkeypatch.setattr(
        watch_play_player_extractor.json,
        "load",
        lambda _: headers,
    )

    return watch_play_player_extractor.WatchPlayPlayerExtractor()


def response(*, text=""):
    result = Mock()
    result.text = text
    return result


def test_extract_returns_tv_video_source(monkeypatch, extractor, player):
    video_url = r"https:\/\/vid.example.com\/st\/episode.m3u8"
    mock_get = Mock(
        return_value=response(
            text=(
                'window.MyPlayerAudio="Dublado";'
                f'url:"{video_url}"'
            )
        )
    )
    monkeypatch.setattr(
        watch_play_player_extractor.requests,
        "get",
        mock_get,
    )

    result = extractor.extract(player)

    assert isinstance(result, TvVideoSource)
    assert result.videoSource == "https://vid.example.com/st/episode.m3u8"
    assert result.hls is None
    assert result.videoImage is None
    assert result.securedLink is None
    assert result.downloadLinks == []
    assert result.attachmentLinks == []
    assert result.ck is None
    mock_get.assert_called_once_with(
        url=player.embed,
        headers={
            "referer": "https://playerflix.ink/serie/1396/2/3",
        },
    )


def test_extract_raises_when_video_url_is_missing(
    monkeypatch,
    extractor,
    player,
):
    mock_get = Mock(
        return_value=response(
            text='window.MyPlayerAudio="Dublado";'
        )
    )
    monkeypatch.setattr(
        watch_play_player_extractor.requests,
        "get",
        mock_get,
    )

    with pytest.raises(
        ValueError,
        match=r"\[PLAYERFLIX TV WATCHPLAY\] ERRO AO EXTRAIR VIDEO URL",
    ):
        extractor.extract(player)

    mock_get.assert_called_once()
