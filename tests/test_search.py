
from unittest.mock import Mock

import pytest
import requests

from embed_movies.tmdb.search import Search
from embed_movies.tmdb.models import Movie, TVShow, SearchResult


@pytest.fixture
def search():
    instance = Search(token="fake_token")
    instance.session.get = Mock()
    return instance


def mock_response(results):
    response = Mock()
    response.json.return_value = {
        "results": results,
        "page": 1,
        "total_pages": 1,
        "total_results": len(results),
    }
    response.raise_for_status.return_value = None
    return response


def test_search_movies(search):
    search.session.get.return_value = mock_response([
        {
            "id": 550,
            "title": "Fight Club",
            "overview": "A movie about an underground fight club.",
            "release_date": "1999-10-15",
            "vote_average": 8.4,
        }
    ])

    results = search.search("Fight Club", media_type="movie")

    assert len(results) == 1
    assert isinstance(results[0], Movie)
    assert results[0].id == 550
    assert results[0].title == "Fight Club"

    search.session.get.assert_called_once()
    assert search.session.get.call_args.kwargs["params"]["query"] == "Fight Club"
    assert search.session.get.call_args.kwargs["params"]["api_key"] == "fake_token"


def test_search_tv_shows(search):
    search.session.get.return_value = mock_response([
        {
            "id": 1396,
            "name": "Breaking Bad",
            "overview": "A chemistry teacher becomes involved in crime.",
            "first_air_date": "2008-01-20",
            "vote_average": 9.0,
        }
    ])

    results = search.search("Breaking Bad", media_type="tv")

    assert len(results) == 1
    assert isinstance(results[0], TVShow)
    assert results[0].id == 1396
    assert results[0].name == "Breaking Bad"


def test_search_multi(search):
    search.session.get.return_value = mock_response([
        {
            "id": 550,
            "media_type": "movie",
            "title": "Fight Club",
        },
        {
            "id": 1396,
            "media_type": "tv",
            "name": "Breaking Bad",
        },
        {
            "id": 17419,
            "media_type": "person",
            "name": "John Doe",
        },
    ])

    results = search.search("test", media_type="multi")

    assert isinstance(results, SearchResult)
    assert len(results.movies) == 1
    assert len(results.tv_shows) == 1
    assert len(results.people) == 1
    assert results.movies[0].title == "Fight Club"
    assert results.tv_shows[0].name == "Breaking Bad"


def test_search_empty_query(search):
    with pytest.raises(ValueError, match="busca não pode estar vazia"):
        search.search("")


def test_search_invalid_media_type(search):
    with pytest.raises(ValueError, match="media_type"):
        search.search("test", media_type="person")


@pytest.mark.parametrize("page", [0, -1])
def test_search_invalid_page(search, page):
    with pytest.raises(ValueError, match="página"):
        search.search("test", page=page)


def test_search_empty_results(search):
    search.session.get.return_value = mock_response([])

    results = search.search("no results", media_type="movie")

    assert results == []


def test_search_http_error(search):
    response = mock_response([])
    response.raise_for_status.side_effect = requests.HTTPError(
        "401 Unauthorized"
    )
    search.session.get.return_value = response

    with pytest.raises(requests.HTTPError):
        search.search("test")


def test_search_missing_token(monkeypatch):
    monkeypatch.delenv("TMDB_TOKEN", raising=False)

    with pytest.raises(ValueError, match="Token"):
        Search(token=None)
