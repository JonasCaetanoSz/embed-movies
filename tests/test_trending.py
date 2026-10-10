
import sys
from pathlib import Path
from unittest.mock import Mock

import pytest
import requests

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "embed_movies" / "src"
sys.path.insert(0, str(SRC))

from tmdb.trending import Trending


@pytest.fixture
def session():
    mock_session = Mock()
    response = Mock()

    response.json.return_value = {
        "page": 1,
        "results": [
            {
                "id": 550,
                "title": "Clube da Luta",
                "media_type": "movie",
            },
            {
                "id": 1399,
                "name": "Game of Thrones",
                "media_type": "tv",
            },
        ],
        "total_pages": 1,
        "total_results": 2,
    }

    mock_session.get.return_value = response
    return mock_session


@pytest.fixture
def trending(session):
    return Trending(token="test-token", session=session)


def test_trending_success(trending, session):
    results = trending.trending()

    assert len(results) == 2
    assert results[0]["title"] == "Clube da Luta"
    assert results[1]["name"] == "Game of Thrones"

    args, kwargs = session.get.call_args

    assert args[0] == (
        "https://api.themoviedb.org/3/trending/all/day"
    )
    assert kwargs["params"]["api_key"] == "test-token"
    assert kwargs["params"]["language"] == "pt-BR"
    assert kwargs["params"]["page"] == 1
    assert kwargs["timeout"] == 10


def test_trending_movies(trending, session):
    results = trending.movies()

    assert len(results) == 2

    args, _ = session.get.call_args
    assert args[0].endswith("/trending/movie/day")


def test_trending_tv_shows(trending, session):
    results = trending.tv_shows()

    assert len(results) == 2

    args, _ = session.get.call_args
    assert args[0].endswith("/trending/tv/day")


def test_trending_all(trending, session):
    trending.all()

    args, _ = session.get.call_args
    assert args[0].endswith("/trending/all/day")


@pytest.mark.parametrize(
    "time_window",
    ["day", "week"],
)
def test_trending_valid_time_window(trending, session, time_window):
    trending.movies(time_window=time_window)

    args, _ = session.get.call_args
    assert args[0].endswith(f"/trending/movie/{time_window}")


def test_trending_custom_language_and_page(trending, session):
    trending.movies(language="en-US", page=2)

    _, kwargs = session.get.call_args

    assert kwargs["params"]["language"] == "en-US"
    assert kwargs["params"]["page"] == 2


@pytest.mark.parametrize("media_type", ["person", "invalid", ""])
def test_trending_invalid_media_type(trending, session, media_type):
    with pytest.raises(ValueError, match="tipo de mídia"):
        trending.trending(media_type=media_type)

    session.get.assert_not_called()


@pytest.mark.parametrize("time_window", ["month", "year", ""])
def test_trending_invalid_time_window(trending, session, time_window):
    with pytest.raises(ValueError, match="período"):
        trending.trending(time_window=time_window)

    session.get.assert_not_called()


@pytest.mark.parametrize("page", [0, -1, True, 1.5, "1"])
def test_trending_invalid_page(trending, session, page):
    with pytest.raises(ValueError, match="página"):
        trending.trending(page=page)

    session.get.assert_not_called()


@pytest.mark.parametrize("language", ["", "   ", None, 123])
def test_trending_invalid_language(trending, session, language):
    with pytest.raises(ValueError, match="idioma"):
        trending.trending(language=language)

    session.get.assert_not_called()


def test_trending_empty_results(trending, session):
    session.get.return_value.json.return_value = {
        "page": 1,
        "results": [],
    }

    assert trending.movies() == []


def test_trending_missing_results(trending, session):
    session.get.return_value.json.return_value = {"page": 1}

    assert trending.movies() == []


def test_trending_http_error(trending, session):
    session.get.return_value.raise_for_status.side_effect = (
        requests.HTTPError("Erro HTTP")
    )

    with pytest.raises(requests.HTTPError):
        trending.movies()


def test_trending_missing_token(monkeypatch):
    monkeypatch.delenv("TMDB_TOKEN", raising=False)

    with pytest.raises(ValueError, match="Token da API do TMDB"):
        Trending(token=None)


def test_trending_uses_environment_token(monkeypatch, session):
    monkeypatch.setenv("TMDB_TOKEN", "environment-token")

    client = Trending(session=session)
    client.movies()

    _, kwargs = session.get.call_args
    assert kwargs["params"]["api_key"] == "environment-token"
