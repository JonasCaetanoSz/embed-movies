import sys
from pathlib import Path
from unittest.mock import Mock

import pytest
import requests

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "embed_movies" / "src"
sys.path.insert(0, str(SRC))

from tmdb.details_episode import DetailsEpisode
from tmdb.models.episode_details import EpisodeDetails


@pytest.fixture
def session():
    mock_session = Mock()
    response = Mock()

    response.json.return_value = {
        "id": 63056,
        "name": "Winter Is Coming",
        "overview": "Ned Stark recebe uma visita inesperada.",
        "episode_number": 1,
        "season_number": 1,
        "air_date": "2011-04-17",
        "still_path": "/example.jpg",
        "vote_average": 8.2,
        "vote_count": 100,
        "runtime": 60,
        "production_code": "101",
        "episode_type": "standard",
        "show_id": 1399,
        "crew": [],
        "guest_stars": [],
        "credits": {},
        "images": {},
        "videos": {},
        "external_ids": {},
    }

    mock_session.get.return_value = response
    return mock_session


@pytest.fixture
def details_episode(session):
    return DetailsEpisode(token="test-token", session=session)


def test_get_episode_success(details_episode, session):
    episode = details_episode.get_episode(1399, 1, 1)

    assert isinstance(episode, EpisodeDetails)
    assert episode.id == 63056
    assert episode.name == "Winter Is Coming"
    assert episode.episode_number == 1
    assert episode.season_number == 1
    assert episode.air_date == "2011-04-17"
    assert episode.still_path == "/example.jpg"
    assert episode.vote_average == 8.2
    assert episode.vote_count == 100
    assert episode.runtime == 60
    assert episode.production_code == "101"
    assert episode.episode_type == "standard"
    assert episode.show_id == 1399

    session.get.assert_called_once()
    args, kwargs = session.get.call_args

    assert args[0] == (
        "https://api.themoviedb.org/3/tv/1399/season/1/episode/1"
    )
    assert kwargs["params"]["api_key"] == "test-token"
    assert kwargs["params"]["language"] == "pt-BR"
    assert kwargs["params"]["append_to_response"] == (
        "credits,images,videos,external_ids"
    )
    assert "Authorization" not in kwargs.get("headers", {})


def test_get_episode_custom_language(details_episode, session):
    details_episode.get_episode(1399, 1, 1, language="en-US")

    _, kwargs = session.get.call_args
    assert kwargs["params"]["language"] == "en-US"


def test_get_episode_still_url(details_episode):
    episode = details_episode.get_episode(1399, 1, 1)

    assert episode.still_url == (
        "https://image.tmdb.org/t/p/original/example.jpg"
    )
    assert episode.still_url_w500 == (
        "https://image.tmdb.org/t/p/w500/example.jpg"
    )


def test_get_episode_without_still(details_episode, session):
    session.get.return_value.json.return_value["still_path"] = None

    episode = details_episode.get_episode(1399, 1, 1)

    assert episode.still_path is None
    assert episode.still_url is None
    assert episode.still_url_w500 is None


def test_get_episode_missing_optional_fields(details_episode, session):
    session.get.return_value.json.return_value = {
        "id": 63056,
        "name": "Episode",
        "episode_number": 1,
        "season_number": 1,
    }

    episode = details_episode.get_episode(1399, 1, 1)

    assert episode.overview == ""
    assert episode.air_date is None
    assert episode.still_path is None
    assert episode.vote_average == 0.0
    assert episode.vote_count == 0
    assert episode.runtime is None
    assert episode.crew == ()
    assert episode.guest_stars == ()
    assert episode.credits is None
    assert episode.images is None
    assert episode.videos is None
    assert episode.external_ids is None


@pytest.mark.parametrize(
    ("tv_id", "season_number", "episode_number"),
    [
        (0, 1, 1),
        (-1, 1, 1),
        (True, 1, 1),
        ("1399", 1, 1),
        (1399, -1, 1),
        (1399, True, 1),
        (1399, 1, 0),
        (1399, 1, -1),
        (1399, 1, True),
    ],
)
def test_get_episode_invalid_ids(
    details_episode,
    session,
    tv_id,
    season_number,
    episode_number,
):
    with pytest.raises(ValueError):
        details_episode.get_episode(
            tv_id=tv_id,
            season_number=season_number,
            episode_number=episode_number,
        )

    session.get.assert_not_called()


def test_get_episode_invalid_language(details_episode, session):
    with pytest.raises(ValueError):
        details_episode.get_episode(
            1399, 1, 1, language=""
        )

    session.get.assert_not_called()


def test_get_episode_http_error(details_episode, session):
    session.get.return_value.raise_for_status.side_effect = (
        requests.HTTPError("404 Not Found")
    )

    with pytest.raises(requests.HTTPError):
        details_episode.get_episode(1399, 1, 1)


def test_get_episode_missing_token(monkeypatch):
    monkeypatch.delenv("TMDB_TOKEN", raising=False)

    with pytest.raises(ValueError, match="token"):
        DetailsEpisode(token=None)


def test_get_episode_specials(details_episode, session):
    details_episode.get_episode(1399, 0, 1)

    args, _ = session.get.call_args
    assert args[0].endswith("/tv/1399/season/0/episode/1")