from .search import Search
from .trending import Trending
from .details_movie import DetailsMovie
from .details_tv import DetailsTV
from .details_season import DetailsSeason
from .details_episode import DetailsEpisode


class TMDBClient(
    Search,
    Trending,
    DetailsMovie,
    DetailsTV,
    DetailsSeason,
    DetailsEpisode,
):
    def __init__(self, api_token: str | None = None):
        super().__init__(token=api_token)