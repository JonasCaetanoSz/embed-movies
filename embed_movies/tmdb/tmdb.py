from .search import Search

class tmdbClient(Search):
    def __init__(self, api_token:str):
        self.api_token = api_token
        super().__init__(token=self.api_token)