
from unittest.mock import Mock

import pytest
import requests

from embed_movies.tmdb.details_movie import DetailsMovie
from embed_movies.tmdb.models import MovieDetails


@pytest.fixture
def details_movie():
    instance = DetailsMovie(token="fake_token")
    instance.session.get = Mock()
    return instance


def test_get_movie_success(details_movie):
    response = Mock()
    response.json.return_value = {
        "id": 550,
        "title": "Clube da Luta",
        "overview": "Um homem insatisfeito com a vida.",
        "release_date": "1999-10-15",
        "runtime": 139,
        "status": "Released",
        "tagline": "Mischief. Mayhem. Soap.",
        "vote_average": 8.4,
        "vote_count": 25000,
        "poster_path": "/poster.jpg",
        "backdrop_path": "/backdrop.jpg",
        "genres": [
            {"id": 18, "name": "Drama"},
            {"id": 53, "name": "Thriller"},
        ],
        "original_language": "en",
        "original_title": "Fight Club",
        "budget": 63000000,
        "revenue": 100853753,
        "homepage": None,
    }

    details_movie.session.get.return_value = response

    movie = details_movie.get_movie(550)

    assert isinstance(movie, MovieDetails)
    assert movie.id == 550
    assert movie.title == "Clube da Luta"
    assert movie.runtime == 139
    assert movie.genres == ("Drama", "Thriller")
    assert movie.poster_url.endswith("/poster.jpg")

    details_movie.session.get.assert_called_once_with(
        "https://api.themoviedb.org/3/movie/550",
        params={
            "api_key": "fake_token",
            "language": "pt-BR",
        },
        timeout=15,
    )

    response.raise_for_status.assert_called_once()


@pytest.mark.parametrize("movie_id", [0, -1])
def test_get_movie_invalid_id(details_movie, movie_id):
    with pytest.raises(ValueError, match="maior que zero"):
        details_movie.get_movie(movie_id)

    details_movie.session.get.assert_not_called()


def test_get_movie_invalid_id_type(details_movie):
    with pytest.raises(TypeError, match="inteiro"):
        details_movie.get_movie("550")

    details_movie.session.get.assert_not_called()


def test_get_movie_http_error(details_movie):
    response = Mock()
    response.raise_for_status.side_effect = requests.HTTPError(
        "Erro na API"
    )

    details_movie.session.get.return_value = response

    with pytest.raises(requests.HTTPError):
        details_movie.get_movie(550)


def test_missing_token(monkeypatch):
    monkeypatch.delenv("TMDB_TOKEN", raising=False)

    with pytest.raises(ValueError, match="Token"):
        DetailsMovie()
