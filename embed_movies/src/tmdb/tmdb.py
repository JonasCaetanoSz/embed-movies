from .search import Search
from .details_movie import DetailsMovie

class tmdbClient(
    Search,
    DetailsMovie
    ):
    def __init__(self, api_token:str):
        self.api_token = api_token
        super().__init__(token=self.api_token)