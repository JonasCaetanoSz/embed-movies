from abc import ABC, abstractmethod
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from ..models import Player, VideoSource

class BasePlayerExtractor(ABC):

    @abstractmethod
    def extract(self, player:Player) -> VideoSource:
        raise NotImplementedError
