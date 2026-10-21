from .movie import MovieAvailablePlayers
from .tv import TvAvailablePlayers

class PlayerFlixClient(
    MovieAvailablePlayers,
    TvAvailablePlayers
):
    pass