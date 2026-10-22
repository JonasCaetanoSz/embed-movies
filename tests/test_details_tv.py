from unittest.mock import Mock

import pytest
import requests

from embed_movies.tmdb.details_tv import DetailsTV
from embed_movies.tmdb.models import TVDetails


@pytest.fixture
def details_tv():
    instance = DetailsTV(token="fake_token")
    instance.session.get = Mock()
    return instance


def test_get_tv_success(details_tv):
    response = Mock()
    response.json.return_value = {
        "id": 1396,
        "name": "Breaking Bad",
        "overview": "Um professor de química começa a produzir drogas.",
        "first_air_date": "2008-01-20",
        "last_air_date": "2013-09-29",
        "status": "Ended",
        "tagline": "Remember my name.",
        "vote_average": 8.9,
        "vote_count": 10000,
        "poster_path": "/poster.jpg",
        "backdrop_path": "/backdrop.jpg",
        "number_of_seasons": 5,
        "number_of_episodes": 62,
        "episode_run_time": [45, 48],
        "genres": [
            {"id": 18, "name": "Drama"},
            {"id": 80, "name": "Crime"},
        ],
        "original_language": "en",
        "original_name": "Breaking Bad",
        "homepage": None,
    }

    details_tv.session.get.return_value = response

    tv = details_tv.get_tv(1396)

    assert isinstance(tv, TVDetails)
    assert tv.id == 1396
    assert tv.name == "Breaking Bad"
    assert tv.number_of_seasons == 5
    assert tv.number_of_episodes == 62
    assert tv.genres == ("Drama", "Crime")
    assert tv.poster_url.endswith("/poster.jpg")

    details_tv.session.get.assert_called_once_with(
        "https://api.themoviedb.org/3/tv/1396",
        params={
            "api_key": "fake_token",
            "language": "pt-BR",
        },
        timeout=15,
    )

    response.raise_for_status.assert_called_once()


@pytest.mark.parametrize("tv_id", [0, -1])
def test_get_tv_invalid_id(details_tv, tv_id):
    with pytest.raises(ValueError, match="maior que zero"):
        details_tv.get_tv(tv_id)

    details_tv.session.get.assert_not_called()


def test_get_tv_invalid_id_type(details_tv):
    with pytest.raises(TypeError, match="inteiro"):
        details_tv.get_tv("1396")

    details_tv.session.get.assert_not_called()


def test_get_tv_http_error(details_tv):
    response = Mock()
    response.raise_for_status.side_effect = requests.HTTPError(
        "Erro na API"
    )

    details_tv.session.get.return_value = response

    with pytest.raises(requests.HTTPError):
        details_tv.get_tv(1396)


def test_missing_token(monkeypatch):
    monkeypatch.delenv("TMDB_TOKEN", raising=False)

    with pytest.raises(ValueError, match="Token"):
        DetailsTV()