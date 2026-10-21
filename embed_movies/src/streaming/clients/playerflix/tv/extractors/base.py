from abc import ABC, abstractmethod
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from ..models import TvPlayer, TvVideoSource

class BasePlayerExtractor(ABC):

    @abstractmethod
    def extract(self, player:TvPlayer) -> TvVideoSource:
        raise NotImplementedError
