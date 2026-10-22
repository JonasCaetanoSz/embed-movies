from abc import ABC, abstractmethod
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from ..models import Movieplayer, MovieVideoSource

class BasePlayerExtractor(ABC):

    @abstractmethod
    def extract(self, player:Movieplayer) -> MovieVideoSource:
        raise NotImplementedError
