from unittest.mock import Mock

import pytest
import requests

from embed_movies.tmdb.details_season import DetailsSeason
from embed_movies.tmdb.models import SeasonDetails, SeasonEpisode


@pytest.fixture
def session():
    return Mock(spec=requests.Session)


@pytest.fixture
def details_season(session):
    return DetailsSeason(token="fake-token", session=session)


def test_get_season_success(details_season, session):
    session.get.return_value.json.return_value = {
        "id": 3624,
        "name": "Pilot",
        "season_number": 1,
        "overview": "Descrição da temporada",
        "air_date": "2008-01-20",
        "poster_path": "/poster.jpg",
        "vote_average": 8.5,
        "episodes": [
            {
                "id": 62085,
                "name": "Pilot",
                "episode_number": 1,
                "season_number": 1,
                "overview": "Primeiro episódio",
                "air_date": "2008-01-20",
                "vote_average": 8.0,
                "still_path": "/episode.jpg",
            }
        ],
    }

    season = details_season.get_season(1396, 1)

    assert isinstance(season, SeasonDetails)
    assert season.id == 3624
    assert season.name == "Pilot"
    assert season.season_number == 1
    assert season.episode_count == 1

    episode = season.episodes[0]

    assert isinstance(episode, SeasonEpisode)
    assert episode.name == "Pilot"
    assert episode.episode_number == 1
    assert episode.still_url.endswith("/episode.jpg")

    session.get.assert_called_once_with(
        "https://api.themoviedb.org/3/tv/1396/season/1",
        params={
            "api_key": "fake-token",
            "language": "pt-BR",
        },
        timeout=15,
    )


def test_get_season_without_episodes(details_season, session):
    session.get.return_value.json.return_value = {
        "id": 3624,
        "name": "Temporada 1",
        "season_number": 1,
        "episodes": [],
    }

    season = details_season.get_season(1396, 1)

    assert season.episodes == ()
    assert season.episode_count == 0


def test_get_season_specials(details_season, session):
    session.get.return_value.json.return_value = {
        "id": 3623,
        "name": "Specials",
        "season_number": 0,
        "episodes": [],
    }

    season = details_season.get_season(1396, 0)

    assert season.season_number == 0


@pytest.mark.parametrize("tv_id", [0, -1])
def test_get_season_invalid_tv_id(details_season, tv_id):
    with pytest.raises(ValueError):
        details_season.get_season(tv_id, 1)


@pytest.mark.parametrize("tv_id", ["1396", None, True])
def test_get_season_invalid_tv_id_type(details_season, tv_id):
    with pytest.raises(TypeError):
        details_season.get_season(tv_id, 1)


def test_get_season_negative_number(details_season):
    with pytest.raises(ValueError):
        details_season.get_season(1396, -1)


@pytest.mark.parametrize(
    "season_number",
    ["1", None, True, 1.5],
)
def test_get_season_invalid_number_type(
    details_season,
    season_number,
):
    with pytest.raises(TypeError):
        details_season.get_season(1396, season_number)


def test_get_season_http_error(details_season, session):
    response = Mock()
    response.raise_for_status.side_effect = requests.HTTPError(
        "Erro HTTP"
    )
    session.get.return_value = response

    with pytest.raises(requests.HTTPError):
        details_season.get_season(1396, 1)


def test_get_season_missing_token(monkeypatch):
    monkeypatch.delenv("TMDB_TOKEN", raising=False)

    with pytest.raises(ValueError):
        DetailsSeason(token="")


def test_season_poster_url(details_season, session):
    session.get.return_value.json.return_value = {
        "id": 3624,
        "name": "Temporada 1",
        "season_number": 1,
        "poster_path": "/poster.jpg",
        "episodes": [],
    }

    season = details_season.get_season(1396, 1)

    assert season.poster_url == (
        "https://image.tmdb.org/t/p/w500/poster.jpg"
    )