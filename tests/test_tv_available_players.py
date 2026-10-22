import json
from unittest.mock import Mock

import pytest
import requests

from embed_movies.streaming.clients.playerflix.tv.extractors import available_players
from embed_movies.streaming.clients.playerflix.tv.models import (
    ResponseDataPlayersTv,
    TvPlayer,
)


@pytest.fixture
def response_data():
    return {
        "status": True,
        "data": {
            "id": 1396,
            "title": {
                "pt": "Breaking Bad",
                "en": "Breaking Bad",
            },
            "release_date": "2008-01-20",
            "options": [
                {
                    "embed": "https://example.com/episode-player",
                    "lang": "pt-br",
                    "label": "WatchPlay",
                    "budget": "success",
                    "icon": '<img src="player.svg" />',
                    "id": "watchplay",
                },
                {
                    "embed": "https://example.com/episode-player-2",
                    "lang": "en",
                    "embed_id": "episode123",
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
            "referer": "https://playerflix.ink/serie/@PATH",
        }
    }

    headers_path = tmp_path / "players_available_headers.json"
    headers_path.write_text(json.dumps(headers), encoding="utf-8")
    monkeypatch.setattr(
        available_players,
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


def test_get_available_players_tv_success(
    monkeypatch,
    response_data,
    mock_response,
    headers_file,
):
    mock_get = Mock(return_value=mock_response)
    monkeypatch.setattr(available_players.requests, "get", mock_get)

    players = available_players.TvAvailablePlayers.get_available_players_tv(
        "1396",
        "2",
        "3",
    )

    assert isinstance(players, list)
    assert len(players) == 2
    assert all(isinstance(player, TvPlayer) for player in players)
    assert players[0].id == "watchplay"
    assert players[0].title == "Breaking Bad"
    assert players[0].release_date == "2008-01-20"
    assert players[0].tv_id == "1396"
    assert players[0].embed_id is None
    assert players[1].id == "embedplayer"
    assert players[1].embed_id == "episode123"
    assert players[1].tv_id == "1396"

    mock_get.assert_called_once_with(
        url="https://playerflix.ink/inc/Ajax.php",
        params={
            "type": "tv",
            "id": "1396",
            "season": "2",
            "episode": "3",
        },
        headers={
            "accept": "*/*",
            "x-requested-with": "XMLHttpRequest",
            "referer": "https://playerflix.ink/serie/2/3",
        },
    )
    assert response_data["data"]["tv_id"] == "1396"


def test_get_available_players_tv_http_error(
    monkeypatch,
    mock_response,
    headers_file,
):
    mock_response.raise_for_status.side_effect = requests.HTTPError(
        "404 Not Found"
    )
    mock_get = Mock(return_value=mock_response)
    monkeypatch.setattr(available_players.requests, "get", mock_get)

    with pytest.raises(requests.HTTPError):
        available_players.TvAvailablePlayers.get_available_players_tv(
            "1396",
            "2",
            "3",
        )

    mock_response.raise_for_status.assert_called_once()


def test_get_available_players_tv_invalid_json(
    monkeypatch,
    mock_response,
    headers_file,
):
    mock_response.json.side_effect = ValueError("JSON inválido")
    mock_get = Mock(return_value=mock_response)
    monkeypatch.setattr(available_players.requests, "get", mock_get)

    with pytest.raises(ValueError, match="JSON inválido"):
        available_players.TvAvailablePlayers.get_available_players_tv(
            "1396",
            "2",
            "3",
        )


def test_response_data_players_tv_model(response_data):
    response = ResponseDataPlayersTv(**response_data)
    response.data["tv_id"] = "1396"

    assert response.status is True
    assert len(response.players) == 2
    assert response.players[0].id == "watchplay"
    assert response.players[0].title == "Breaking Bad"
    assert response.players[0].tv_id == "1396"
    assert response.players[1].embed_id == "episode123"
