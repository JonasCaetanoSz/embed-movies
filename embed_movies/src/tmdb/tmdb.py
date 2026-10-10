from .search import Search
from .details_movie import DetailsMovie
from .details_tv import DetailsTV
from .details_season import DetailsSeason
from .details_episode import DetailsEpisode


class TMDBClient(
    Search,
    DetailsMovie,
    DetailsTV,
    DetailsSeason,
    DetailsEpisode,
):
    def __init__(self, api_token: str):
        super().__init__(token=api_token)