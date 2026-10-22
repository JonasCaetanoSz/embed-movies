
import json
import sys
from pathlib import Path
from unittest.mock import Mock

import pytest

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "embed_movies" / "src"
sys.path.insert(0, str(SRC))

from streaming.clients.playerflix.movie.extractors import (
    vip_player_player_extractor,
)
from streaming.clients.playerflix.movie.models import MovieVideoSource


@pytest.fixture
def player():
    return Mock(
        embed=(
            "https://embedplayer2.xyz/video/"
            "ff4d3dab210c762071b8f711584298cc"
        )
    )


@pytest.fixture
def extractor(monkeypatch):
    headers = {
        "get_player": {
            "accept": "*/*",
            "origin": "https://embedplayer2.xyz",
            "x-requested-with": "XMLHttpRequest",
        }
    }

    monkeypatch.setattr(
        vip_player_player_extractor.json,
        "load",
        lambda _: headers,
    )

    return vip_player_player_extractor.VipPlayerPlayerExtractor()


def response(data=None):
    result = Mock()
    result.json.return_value = data
    return result


def test_extract_returns_video_source(
    monkeypatch,
    extractor,
    player,
):
    video_data = {
        "hls": True,
        "videoImage": "https://cdn.example/poster.jpg",
        "videoSource": "https://cdn.example/video.m3u8",
        "securedLink": "https://cdn.example/secured",
        "downloadLinks": ["https://cdn.example/download"],
        "attachmentLinks": [],
        "ck": "signature",
    }

    mock_post = Mock(return_value=response(video_data))

    monkeypatch.setattr(
        vip_player_player_extractor.requests,
        "post",
        mock_post,
    )

    result = extractor.extract(player)

    assert isinstance(result, MovieVideoSource)
    assert result.videoSource == video_data["videoSource"]
    assert result.videoImage == video_data["videoImage"]
    assert result.downloadLinks == video_data["downloadLinks"]

    mock_post.assert_called_once_with(
        "https://embedplayer2.xyz/player/index.php",
        params={
            "data": "ff4d3dab210c762071b8f711584298cc",
            "do": "getVideo",
        },
        headers={
            "accept": "*/*",
            "origin": "https://embedplayer2.xyz",
            "x-requested-with": "XMLHttpRequest",
        },
        data={
            "hash": "ff4d3dab210c762071b8f711584298cc",
            "r": "",
        },
    )


def test_extract_raises_when_video_hash_is_missing(
    monkeypatch,
    extractor,
):
    player = Mock(embed="https://embedplayer2.xyz/invalid")
    mock_post = Mock()

    monkeypatch.setattr(
        vip_player_player_extractor.requests,
        "post",
        mock_post,
    )

    with pytest.raises(
        ValueError,
        match="ERRO AO EXTRAIR VIDEO HASH",
    ):
        extractor.extract(player)

    mock_post.assert_not_called()


def test_extract_loads_headers_from_json(
    monkeypatch,
    extractor,
    player,
):
    video_data = {
        "hls": True,
        "videoImage": "https://cdn.example/poster.jpg",
        "videoSource": "https://cdn.example/video.m3u8",
        "securedLink": "https://cdn.example/secured",
        "downloadLinks": [],
        "attachmentLinks": [],
        "ck": "signature",
    }

    mock_post = Mock(return_value=response(video_data))
    monkeypatch.setattr(
        vip_player_player_extractor.requests,
        "post",
        mock_post,
    )

    extractor.extract(player)

    assert mock_post.call_args.kwargs["headers"]["origin"] == (
        "https://embedplayer2.xyz"
    )
