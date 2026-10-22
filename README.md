# 🎬 Embed Movies

Biblioteca Python para consultar informações de filmes e séries por meio da API do TMDB e integrar clientes de streaming para localizar players e extrair fontes de vídeo.

[![Python](https://img.shields.io/badge/Python-3.10%2B-blue.svg)](https://www.python.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## ✨ Funcionalidades

- 🔎 Busca de filmes e séries pelo TMDB.
- 🔥 Consulta de filmes e séries em alta.
- 🎞️ Consulta de detalhes de filmes.
- 📺 Consulta de séries, temporadas e episódios.
- 🌐 Integração com clientes de streaming.
- ▶️ Consulta de players disponíveis para filmes e episódios de séries.
- 🔗 Extração de fontes de vídeo, incluindo playlists M3U8.
- 🧪 Testes automatizados com `pytest`.

## 📦 Instalação

### Instalar diretamente do GitHub

Instale a versão mais recente da branch `main` diretamente pelo Git:

```bash
python -m pip install "git+https://github.com/JonasCaetanoSz/embed-movies.git"
```

### Instalação para desenvolvimento

Clone o repositório:

```bash
git clone https://github.com/JonasCaetanoSz/embed-movies.git
cd embed-movies
```

Crie e ative um ambiente virtual.

**Windows — PowerShell**

```powershell
python -m venv .venv
.venv\Scripts\Activate.ps1
```

**Linux/macOS**

```bash
python -m venv .venv
source .venv/bin/activate
```

Instale o projeto em modo editável:

```bash
python -m pip install -e .
```

## ⚙️ Configuração do TMDB

Para utilizar os módulos do TMDB, você precisa de um token de API.

Obtenha suas credenciais na página oficial: [TMDB API](https://www.themoviedb.org/settings/api).

**Windows — PowerShell**

```powershell
$env:TMDB_TOKEN = "SEU_TOKEN_AQUI"
```

**Linux/macOS**

```bash
export TMDB_TOKEN="SEU_TOKEN_AQUI"
```

A variável fica disponível no terminal atual. Os exemplos abaixo recebem o token diretamente pelo construtor; definir a variável de ambiente, por si só, não garante que a biblioteca a leia automaticamente.

## 🚀 Como usar

### 1. Buscar filmes e séries

```python
from embed_movies.tmdb import Search

search = Search(token="SEU_TOKEN_AQUI")

movies = search.search("Fight Club", media_type="movie")

for movie in movies:
    print(movie.title, movie.id)
```

O parâmetro `media_type` aceita:

| Valor | Conteúdo |
|---|---|
| `movie` | Filmes |
| `tv` | Séries |
| `multi` | Diferentes tipos de conteúdo |

### 2. Consultar conteúdos em alta

```python
from embed_movies.tmdb import Trending

trending = Trending(token="SEU_TOKEN_AQUI")

movies = trending.movies()
series = trending.tv_shows()
all_content = trending.all()

print(movies)
print(series)
print(all_content)
```

O parâmetro `time_window` aceita `day` para tendências diárias e `week` para tendências semanais.

```python
movies = trending.movies(time_window="week")
```

### 3. Consultar detalhes de filmes

```python
from embed_movies.tmdb import DetailsMovie

details = DetailsMovie(token="SEU_TOKEN_AQUI")
movie = details.get_movie(550)

print(movie.title)
print(movie.overview)
print(movie.runtime)
print(movie.genres)
```

O identificador `550` corresponde ao filme *Clube da Luta* no TMDB.

### 4. Consultar detalhes de séries

```python
from embed_movies.tmdb import DetailsTV

details = DetailsTV(token="SEU_TOKEN_AQUI")
series = details.get_tv(1396)

print(series.name)
print(series.number_of_seasons)
print(series.number_of_episodes)
```

O identificador `1396` corresponde à série *Breaking Bad* no TMDB.

### 5. Consultar temporadas

```python
from embed_movies.tmdb import DetailsSeason

details = DetailsSeason(token="SEU_TOKEN_AQUI")
season = details.get_season(1396, 1)

print(season.name)
print(season.season_number)
print(season.episodes)
```

Os argumentos representam o identificador da série e o número da temporada.

### 6. Consultar episódios

```python
from embed_movies.tmdb import DetailsEpisode

details = DetailsEpisode(token="SEU_TOKEN_AQUI")
episode = details.get_episode(1396, 1, 1)

print(episode.name)
print(episode.overview)
print(episode.air_date)
```

Os argumentos representam o identificador da série, o número da temporada e o número do episódio.

## 📺 Clientes de streaming

### [PlayerFlix](https://playerflix.ink)

**Status da integração:** 🟢 Implementada

O cliente PlayerFlix permite consultar os players disponíveis para filmes e episódios de séries e extrair fontes de vídeo usando o método `get_stream()`.

#### Players disponíveis

| Player | Status |
|---|---|
| WatchPlay | 🟢 Implementado |
| Premium | 🟢 Implementado |
| VIP Player | 🟢 Implementado |

O status indica que o player possui suporte na implementação da biblioteca, não uma garantia de disponibilidade contínua no site externo.

#### Importação

```python
from embed_movies.streaming.clients import PlayerFlixClient
```

#### Obtendo players disponíveis para um filme

Informe o ID do filme no TMDB:

```python
players = PlayerFlixClient.get_available_players_movie("550")

for player in players:
    print(f"Player: {player.label}")
```

#### Obtendo players disponíveis para um episódio de série

Informe o ID da série no TMDB, a temporada e o episódio:

```python
players = PlayerFlixClient.get_available_players_tv(
    tv_id="1399",
    season="1",
    episode="1",
)

for player in players:
    print(f"Player: {player.label}")
```

#### Extraindo a fonte de vídeo com `get_stream()`

Cada objeto de player disponibiliza o método `get_stream()`, que seleciona o extrator correspondente ao player.

O exemplo a seguir tenta extrair a fonte de cada player disponível para um filme:

```python
players = PlayerFlixClient.get_available_players_movie("550")

for player in players:
    try:
        stream = player.get_stream()

        print(f"Player: {player.label}")
        print(stream)

    except Exception as error:
        print(f"Erro ao extrair {player.label}: {error}")
```

Para extrair a fonte de um episódio de série, utilize `get_available_players_tv()`:

```python
players = PlayerFlixClient.get_available_players_tv(
    tv_id="1399",
    season="1",
    episode="1",
)

for player in players:
    try:
        stream = player.get_stream()

        print(f"Player: {player.label}")
        print(stream)

    except Exception as error:
        print(f"Erro ao extrair {player.label}: {error}")
```

#### Selecionando um player específico

É possível selecionar um player pelo atributo `label`, como `WatchPlay`, `Premium` ou `VIP Player`.

Exemplo selecionando o WatchPlay para um filme:

```python
players = PlayerFlixClient.get_available_players_movie("550")

player = next(
    (p for p in players if p.label == "WatchPlay"),
    None,
)

if player is not None:
    stream = player.get_stream()
    print(stream)
else:
    print("WatchPlay não está disponível para este filme.")
```

Para selecionar outro player, substitua `"WatchPlay"` pelo `label` desejado.

#### Tipos de retorno

O método `get_stream()` retorna:

- `MovieVideoSource` para filmes.
- `TvVideoSource` para episódios de séries.

A extração depende do extrator implementado para o player. Caso não exista um extrator registrado para determinado player, poderá ser lançada uma exceção `NotImplementedError`.

As fontes extraídas podem conter URLs de playlists M3U8. A reprodução pode depender de cabeçalhos HTTP, cookies, sessões válidas e das condições de acesso do provedor. A URL obtida não garante disponibilidade permanente.

#### Acessando a URL da transmissão

O método `get_stream()` retorna um objeto `MovieVideoSource` para filmes ou `TvVideoSource` para episódios de séries. A URL real da transmissão fica no atributo `videoSource` desse objeto.

```python
players = PlayerFlixClient.get_available_players_movie("550")

player = next(
    (p for p in players if p.label == "WatchPlay"),
    None,
)

if player is not None:
    video_source = player.get_stream()
    stream_url = video_source.videoSource

    print(stream_url)
```

Para episódios de séries, utilize `get_available_players_tv()` e acesse o mesmo atributo `videoSource` do objeto `TvVideoSource`.



## 🧪 Testes

Para executar os testes automatizados, instale o projeto em modo de desenvolvimento e execute:

```bash
python -m pytest
```

Para obter uma saída mais detalhada:

```bash
python -m pytest -v
```

## 🗂️ Estrutura do projeto

```text
embed-movies/
├── embed_movies/
│   └── src/
│       └── embed_movies/
│           ├── tmdb/
│           │   ├── models/
│           │   ├── search.py
│           │   ├── trending.py
│           │   ├── details_movie.py
│           │   ├── details_tv.py
│           │   ├── details_season.py
│           │   └── details_episode.py
│           └── streaming/
│               └── clients/
├── tests/
├── pyproject.toml
├── README.md
└── LICENSE
```

- `tmdb/`: integração com a API do TMDB e modelos de dados.
- `streaming/`: clientes e extratores de fontes de vídeo.
- `tests/`: testes automatizados.
- `pyproject.toml`: metadados, dependências e configurações de empacotamento.

## 🛠️ Tecnologias

- Python 3.10+
- Requests
- Nodriver
- MSS
- Pytest

As dependências declaradas no `pyproject.toml` são instaladas automaticamente durante a instalação do pacote.

## 🤝 Contribuindo

Contribuições são bem-vindas!

1. Faça um fork do repositório.
2. Crie uma branch para sua alteração.
3. Implemente a funcionalidade ou correção.
4. Adicione ou atualize os testes.
5. Envie um pull request.

Exemplo:

```bash
git checkout -b feat/nova-funcionalidade
git add .
git commit -m "feat: add nova funcionalidade"
git push origin feat/nova-funcionalidade
```

Depois, abra um pull request no GitHub.

## ⚠️ Aviso legal

Esta biblioteca integra APIs e serviços externos. Utilize-a respeitando os termos de uso dos provedores e a legislação aplicável. A disponibilidade das fontes de vídeo depende dos respectivos serviços.

## 📄 Licença

Este projeto está licenciado sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE) para obter os detalhes.

## 🔗 Links

- [Repositório no GitHub](https://github.com/JonasCaetanoSz/embed-movies)
- [Reportar um problema](https://github.com/JonasCaetanoSz/embed-movies/issues)
- [Documentação do TMDB](https://developer.themoviedb.org/docs)
