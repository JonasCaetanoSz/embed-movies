from abc import ABC, abstractmethod
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from ..models import Player

class BasePlayerExtractor(ABC):

    @abstractmethod
    def extract(self, player:Player) -> str:
        raise NotImplementedError
