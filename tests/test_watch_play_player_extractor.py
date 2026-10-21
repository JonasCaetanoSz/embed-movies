import sys
from pathlib import Path
from unittest.mock import Mock

import pytest

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "embed_movies" / "src"
sys.path.insert(0, str(SRC))

from streaming.clients.playerflix.movie.extractors import (
    watch_play_player_extractor,
)
from streaming.clients.playerflix.movie.models.movie_video_source import MovieVideoSource


@pytest.fixture
def player():
    return Mock(
        movie_id="movie-123",
        embed="https://example.com/embed",
    )


@pytest.fixture
def extractor(monkeypatch):
    headers = {
        "player_page": {
            "referer": "https://superflixapi.fun/filme/@ID",
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


def test_extract_returns_video_source(monkeypatch, extractor, player):
    video_url = r"https:\/\/vid.example.com\/st\/playlist.m3u8"
    player_page = response(
        text=(
            'window.MyPlayerAudio="Dublado";'
            f'url:"{video_url}"'
        )
    )

    mock_get = Mock(return_value=player_page)
    monkeypatch.setattr(
        watch_play_player_extractor.requests,
        "get",
        mock_get,
    )

    result = extractor.extract(player)

    assert isinstance(result, MovieVideoSource)
    assert result.videoSource == "https://vid.example.com/st/playlist.m3u8"
    assert result.hls is None
    assert result.videoImage is None
    assert result.securedLink is None
    assert result.downloadLinks == []
    assert result.attachmentLinks == []
    assert result.ck is None

    mock_get.assert_called_once_with(
        url=player.embed,
        headers={
            "referer": "https://superflixapi.fun/filme/@ID",
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
        match=r"\[PLAYERFLIX MOVIE WATCHPLAY\] ERRO AO EXTRAIR VIDEO URL",
    ):
        extractor.extract(player)

    mock_get.assert_called_once()