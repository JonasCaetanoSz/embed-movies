from .search import Search
from .details_movie import DetailsMovie
from .details_tv import DetailsTV


class tmdbClient(
    Search,
    DetailsMovie,
    DetailsTV,
):
    def __init__(self, api_token: str):
        super().__init__(token=api_token)