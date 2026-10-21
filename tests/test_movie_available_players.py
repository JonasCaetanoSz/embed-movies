
import json
import sys
from pathlib import Path
from unittest.mock import Mock, patch

import pytest
import requests

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "embed_movies" / "src"
sys.path.insert(0, str(SRC))

from streaming.clients.playerflix.movie.extractors.available_players import (
    MovieAvailablePlayers,
)
from streaming.clients.playerflix.movie.models import (
    Movieplayer,
    ResponseDataPlayersMovie,
)


@pytest.fixture
def response_data():
    return {
        "status": True,
        "data": {
            "id": 1701,
            "title": {
                "pt": "Os Simpsons: O Filme",
                "en": "The Simpsons Movie",
            },
            "bgImage": "https://example.com/movie.jpg",
            "release_date": "2007-07-25",
            "seasons": None,
            "season_count": None,
            "options": [
                {
                    "embed": "https://example.com/player",
                    "lang": "pt-br",
                    "label": "WatchPlay",
                    "budget": "success",
                    "icon": '<img src="player.svg" />',
                    "id": "watchplay",
                },
                {
                    "embed": "https://example.com/player2",
                    "lang": "pt-br",
                    "embed_id": "abc123",
                    "label": "VIP Player",
                    "budget": "success",
                    "icon": '<img src="player2.svg" />',
                    "id": "embedplayer",
                },
            ],
        },
    }


@pytest.fixture
def headers_file(tmp_path, monkeypatch):
    headers = {
        "get_players": {
            "accept": "*/*",
            "x-requested-with": "XMLHttpRequest",
            "referer": "https://playerflix.ink/filme/@ID",
        }
    }

    headers_path = tmp_path / "players_available_headers.json"
    headers_path.write_text(
        json.dumps(headers),
        encoding="utf-8",
    )

    module = sys.modules[
        "streaming.clients.playerflix.movie.extractors.available_players"
    ]

    monkeypatch.setattr(
        module,
        "__file__",
        str(tmp_path / "available_players.py"),
    )

    return headers_path


@pytest.fixture
def mock_response(response_data):
    response = Mock()
    response.json.return_value = response_data
    response.raise_for_status.return_value = None
    return response


@pytest.fixture
def movie_available_players():
    return MovieAvailablePlayers


@patch(
    "streaming.clients.playerflix.movie.extractors.available_players.requests.get"
)
def test_get_available_players_movie_success(
    mock_get,
    mock_response,
    headers_file,
    movie_available_players,
):
    mock_get.return_value = mock_response

    players = movie_available_players.get_available_players_movie("35")

    assert isinstance(players, list)
    assert len(players) == 2
    assert all(isinstance(player, Movieplayer) for player in players)

    assert players[0].id == "watchplay"
    assert players[0].title == "Os Simpsons: O Filme"
    assert players[0].release_date == "2007-07-25"
    assert players[0].label == "WatchPlay"
    assert players[0].embed == "https://example.com/player"
    assert players[0].embed_id is None

    assert players[1].id == "embedplayer"
    assert players[1].label == "VIP Player"
    assert players[1].embed_id == "abc123"

    mock_get.assert_called_once()

    args, kwargs = mock_get.call_args

    assert args == ()
    assert kwargs["url"] == "https://playerflix.ink/inc/Ajax.php"
    assert kwargs["params"] == {
        "type": "movie",
        "id": "35",
        "season": "null",
        "episode": "null",
    }
    assert kwargs["headers"] == {
        "accept": "*/*",
        "x-requested-with": "XMLHttpRequest",
        "referer": "https://playerflix.ink/filme/35",
    }


@patch(
    "streaming.clients.playerflix.movie.extractors.available_players.requests.get"
)
def test_get_available_players_movie_http_error(
    mock_get,
    mock_response,
    headers_file,
    movie_available_players,
):
    mock_response.raise_for_status.side_effect = requests.HTTPError(
        "404 Not Found"
    )
    mock_get.return_value = mock_response

    with pytest.raises(requests.HTTPError):
        movie_available_players.get_available_players_movie("35")

    mock_get.return_value.raise_for_status.assert_called_once()


@patch(
    "streaming.clients.playerflix.movie.extractors.available_players.requests.get"
)
def test_get_available_players_movie_invalid_json(
    mock_get,
    mock_response,
    headers_file,
    movie_available_players,
):
    mock_response.json.side_effect = ValueError("JSON inválido")
    mock_get.return_value = mock_response

    with pytest.raises(ValueError, match="JSON inválido"):
        movie_available_players.get_available_players_movie("35")


def test_response_data_players_model(response_data):
    response = ResponseDataPlayersMovie(**response_data)

    assert response.status is True
    assert len(response.players) == 2

    assert response.players[0].id == "watchplay"
    assert response.players[0].title == "Os Simpsons: O Filme"
    assert response.players[0].release_date == "2007-07-25"

    assert response.players[1].id == "embedplayer"
    assert response.players[1].embed_id == "abc123"
