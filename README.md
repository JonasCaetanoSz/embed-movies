#  📦 Embed-movies - biblioteca para streaming de Filmes e Series

Biblioteca Java para obter embeds de filmes e séries a partir de dados externos usando scraping 


## ✅ Conteúdo deste README

- [Instalação](#️-instalação)
- [Importando no projeto](#-importando-no-projeto-maven)
- [Configurações](#️-configurações)
- [Exemplos de uso](#exemplos-de-uso)
- [Visão geral das classes](#-visão-geral-das-classes)
- [Contribuições](#-contribuições)
- [Contato](#contato)
- [Licença](#-licença)

## 🛠️ Instalação

- Clone o repositorio 

```bash
git clone https://github.com/JonasCaetanoSz/embed-movies.git
```

- Vá para o diretorio do projeto

```bash
cd embed-movies
```

Instale a biblioteca usando o [Maven](https://maven.apache.org/download.cgi):

```bash
mvn install
```

**Observação:** Precisa de JDK 17 ou superior instalado pra compilar.


## 📦 Importando no projeto (Maven)

Adicione a dependência no seu `pom.xml`:

```xml
<dependency>
    <groupId>com.github.jonascaetanosz</groupId>
    <artifactId>embedmovies</artifactId>
    <version>1.0</version>
</dependency>
```

## ⚙️ Configurações


```java

import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;

 String tmdb_api_key = "sua-chave-de-api-tmdb-aqui";
 TmdbConfig.setApiKey(tmdb_api_key);
```

**Observação:** O token deve ser obtido no site do  [TMDB](https://www.themoviedb.org/?language=pt-BR).


## Exemplos de uso

# Conteudos em alta

```java
import java.util.List;

import com.github.jonascaetanosz.embedmovies.tmdb.Trending;
import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;
import com.github.jonascaetanosz.embedmovies.tmdb.models.Media;


public class TrendingTest {
    public static void main(String[] args) {
        String tmdb_api_key = "sua-chave-de-api-tmdb-aqui";
        TmdbConfig.setApiKey(tmdb_api_key);
        List<Media> medias = Trending.all();

        for (Media media : medias){
            String mediaType = media.getMedia_type().toUpperCase();
            String mediaName= media.getName();
            String mediaID = media.getId();
            System.out.println( String.format("[%s - %s ] %s ", mediaID, mediaType, mediaName) );
        }
    }
}
```

# Buscar filmes e series

```java
import java.util.List;

import com.github.jonascaetanosz.embedmovies.tmdb.Search;
import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;
import com.github.jonascaetanosz.embedmovies.tmdb.models.Media;

public class SearchTest {
    public static void main(String[] args) {
        String tmdb_api_key = "sua-chave-de-api-tmdb-aqui";
        TmdbConfig.setApiKey(tmdb_api_key);
        List<Media> medias = Search.search("os simpsons");

        for (Media media : medias){
            String mediaType = media.getMedia_type().toUpperCase();
            String mediaName= media.getName();
            String mediaID = media.getId();
            System.out.println( String.format("[%s - %s ] %s ", mediaID, mediaType, mediaName) );
        }
    }
}
```

# Detalhar filme

```java
import com.github.jonascaetanosz.embedmovies.filmes.models.Movie;
import com.github.jonascaetanosz.embedmovies.filmes.models.ProductionCompanies;
import com.github.jonascaetanosz.embedmovies.tmdb.DetailsMovie;
import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;

import utils.LoadConfigs;

public class MovieDetailsTest {
    public static void main(String[] args) {
        String tmdb_api_key = "sua-chave-de-api-tmdb-aqui";
        TmdbConfig.setApiKey(tmdb_api_key);
        Movie movie = DetailsMovie.details("35");

        System.out.println( String.format( "\ntitulo: %s", movie.getTitle() ) );
        System.out.println( String.format( "\noverview: %s", movie.getOverview() ) );
        System.out.println( String.format( "\nimdb id: %s", movie.getImdb_id() ) );
        System.out.println( String.format( "\npais de origem: %s", movie.getOrigin_country() ) );
        System.out.println( String.format( "\noriginal idioma: %s", movie.getOriginal_language() ) );
        System.out.println( String.format( "\ntitulo original: %s", movie.getOriginal_title() ) );
        System.out.println( String.format( "\npopularidade: %s", movie.getPopularity() ) );
        System.out.println( String.format( "\nposter_path: %s", movie.getPoster_path() ) );
        System.out.println( String.format("poster url: %s", movie.getPoster_url() ) );
        System.out.println( String.format("backdrop url: %s", movie.getBackdrop_url() ) );

        System.out.println( "\n----empresas de produção:----\n" );

        for (ProductionCompanies company : movie.getProduction_companies()){
            System.out.println( String.format("nome: %s", company.getName() ) );
            System.out.println( String.format("ID: %s", company.getId() ) );
            System.out.println( String.format("logo path: %s", company.getLogo_path() ) );
            System.out.println( String.format("pais de origem: %s\n", company.getOrigin_country() ) );
            System.out.println( String.format("logo url: %s", company.getLogo_url() ) );

        }
        System.out.println("\n----fim empresas que produziu----\n");

        System.out.println( String.format( "\nstatus: %s", movie.getStatus() ) );
        System.out.println( String.format( "\ntagline: %s", movie.getTagline())  );
        System.out.println( String.format( "\nmedia de votação: %s", movie.getVote_average() ) );
        System.out.println( String.format( "\ncontagem de votos: %s", movie.getVote_count() ) );


    }
}

```

# Detalhar serie

```java
import com.github.jonascaetanosz.embedmovies.tv.models.Tv;
import com.github.jonascaetanosz.embedmovies.tv.models.ProductionCompany;
import com.github.jonascaetanosz.embedmovies.tmdb.DetailsTv;
import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;

import utils.LoadConfigs;

public class TvDetailsTest {
    public static void main(String[] args) {
        String tmdb_api_key = LoadConfigs.load("config_test.ini", "tmdb", "api_key");
        TmdbConfig.setApiKey(tmdb_api_key);
        Tv serie = DetailsTv.details("71446");

        System.out.println( String.format( "\ntitulo: %s", serie.getName() ) );
        System.out.println( String.format( "\noverview: %s", serie.getOverview() ) );
        System.out.println( String.format( "\nimdb id: %s", serie.getId() ) );
        System.out.println( String.format( "\npais de origem: %s", serie.getOrigin_country() ) );
        System.out.println( String.format( "\noriginal idioma: %s", serie.getOriginal_language() ) );
        System.out.println( String.format( "\ntitulo original: %s", serie.getOriginal_name() ) );
        System.out.println( String.format( "\npopularidade: %s", serie.getPopularity() ) );
        System.out.println( String.format( "\nposter_path: %s", serie.getPoster_path() ) );
        System.out.println( String.format("poster url: %s", serie.getPoster_url() ) );
        System.out.println( String.format("backdrop url: %s", serie.getBackdrop_url() ) );

        System.out.println( "\n----empresas de produção:----\n" );

        for (ProductionCompany company : serie.getProduction_companies()){
            System.out.println( String.format("nome: %s", company.getName() ) );
            System.out.println( String.format("ID: %s", company.getId() ) );
            System.out.println( String.format("logo path: %s", company.getLogo_path() ) );
            System.out.println( String.format("pais de origem: %s\n", company.getOrigin_country() ) );
            System.out.println( String.format("logo url: %s", company.getLogo_url() ) );

        }
        System.out.println("\n----fim empresas que produziu----\n");

        System.out.println( String.format( "\nstatus: %s", serie.getStatus() ) );
        System.out.println( String.format( "\ntagline: %s", serie.getTagline())  );
        System.out.println( String.format( "\nmedia de votação: %s", serie.getVote_average() ) );
        System.out.println( String.format( "\ncontagem de votos: %s", serie.getVote_count() ) );


    }
}
```

# Detalhar temporadas de serie

```java
import java.util.List;

import com.github.jonascaetanosz.embedmovies.tmdb.DetailsTvSeasons;
import com.github.jonascaetanosz.embedmovies.tmdb.models.Season;

import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;
import utils.LoadConfigs;

public class DetailsTvSeasonsTest {
    public static void main(String[] args) {

        String tmdb_api_key = LoadConfigs.load("config_test.ini", "tmdb", "api_key");
        TmdbConfig.setApiKey(tmdb_api_key);
        String tmdbID = "71446";
        List<Season> seasons = DetailsTvSeasons.getAllSeasons(tmdbID);
        
        for (int i = 0; i < seasons.size(); i++) {
            Season season = seasons.get(i);
            System.out.println(String.format("------- %s -------", season.getName().toUpperCase() ));
            System.out.println(String.format("Número de episódios: %d", season.getEpisodeCount()));
            System.out.println(String.format("ID: %d", season.getId()));
            System.out.println(String.format("Poster path: %s", season.getPosterPath()));
            System.out.println(String.format("Poster url: %s", season.getPosterUrl()));
            System.out.println(String.format("Data de estreia: %s", season.getAirDate()));
            System.out.println(String.format("Número da temporada: %d", season.getSeasonNumber()));
            System.out.println(String.format("Nota média: %.1f", season.getVoteAverage()));
            System.out.println();
        }
    }
}

```

# Detalhar episodios de serie

```java
import com.github.jonascaetanosz.embedmovies.tmdb.DetailsTvEpisodes;
import com.github.jonascaetanosz.embedmovies.tmdb.models.Episode;
import com.github.jonascaetanosz.embedmovies.tv.models.Tv;

import java.util.List;

import com.github.jonascaetanosz.embedmovies.tmdb.DetailsTv;
import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;
import utils.LoadConfigs;

public class DetailsTvEpisodesTest {
    public static void main(String[] args) {
        String tmdb_api_key = LoadConfigs.load("config_test.ini", "tmdb", "api_key");
        TmdbConfig.setApiKey(tmdb_api_key);

        String tmdbID = "71446";
        String season = "1";

        Tv serie = DetailsTv.details(tmdbID);
        System.out.println("Série: " + serie.getName());
        System.out.println("====================================\n");

        List<Episode> episodes = DetailsTvEpisodes.details(tmdbID, season);

        for (Episode episode : episodes) {
            System.out.println("Episódio: " + episode.getEpisode_number());
            System.out.println("banner: " + episode.getStill_url());
            System.out.println("Título: " + episode.getName());
            System.out.println("Data de exibição: " + episode.getAir_date());
            System.out.println("Duração: " + episode.getRuntime() + " minutos");
            System.out.println("Nota média: " + episode.getVote_average());
            System.out.println("Resumo: " + episode.getOverview());
            System.out.println("------------------------------------\n");
        }
    }
}

```
# Streams disponiveis (filme)

```java
import java.util.List;

import com.github.jonascaetanosz.embedmovies.filmes.streamsAvailables;
import com.github.jonascaetanosz.embedmovies.filmes.models.Stream;

public class MovieStreamsAvailablesTest {
    public static void main(String[] args) {
       List<Stream> streams =  streamsAvailables.getStreams("35");

       for (Stream stream : streams){
        System.err.println("\n---- player detalhes -----");
        System.out.println( String.format("transmissão link: %s", stream.getStreamUrl() ) );
        System.out.println( String.format("nome da transmissão: %s", stream.getStreamName() ) );
        System.out.println( String.format("descricao da transmissão: %s", stream.getStreamDescription() ) );
        System.out.println( String.format("titulo do filme: %s", stream.getMovieTitle() ) );

       }
    }
}
```

# Streams disponiveis (serie)

```java
import java.util.List;

import com.github.jonascaetanosz.embedmovies.tv.streamsAvailables;
import com.github.jonascaetanosz.embedmovies.tv.models.Stream;

public class TvStreamsAvailablesTest {
    public static void main(String[] args) {

        String tmdbID = "71446";
        String episodeNumber = "2";
        String seasonNumber = "1";
       List<Stream> streams =  streamsAvailables.getStreams(tmdbID,seasonNumber, episodeNumber);

       for (Stream stream : streams){
        System.err.println("\n---- player detalhes -----");
        System.out.println( String.format("transmissão link: %s", stream.getStreamUrl() ) );
        System.out.println( String.format("nome da transmissão: %s", stream.getStreamName() ) );
        System.out.println( String.format("descricao da transmissão: %s", stream.getStreamDescription() ) );
        System.out.println( String.format("titulo do episodio: %s", stream.getEpisodeTitle() ) );

       }
    }
}

```
# Donwload de Filme

```java

import java.nio.file.Paths;

import com.github.jonascaetanosz.embedmovies.filmes.DownloadMovie;
import com.github.jonascaetanosz.embedmovies.filmes.models.DownloadResult;
import com.github.jonascaetanosz.embedmovies.filmes.models.Movie;
import com.github.jonascaetanosz.embedmovies.filmes.models.Stream;
import com.github.jonascaetanosz.embedmovies.tmdb.DetailsMovie;
import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;

import java.nio.file.Path;


public class DownloadMovieTest {
    public static void main(String[] args) {


        String tmdb_api_key = "sua-chave-de-api-tmdb-aqui";
        TmdbConfig.setApiKey(tmdb_api_key);
        String tmdbID = "35";
        Movie tmdbMovie = DetailsMovie.details( tmdbID );
        String safeTitle = tmdbMovie.getTitle().replaceAll("[\\\\/:*?\"<>|]", "_");
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads");
        Path movieDir = downloadsPath.resolve(safeTitle);
        DownloadResult result = DownloadMovie.startDownload(tmdbID, movieDir);
        if (result == null){
            String messsage = String.format("[%s - download falhou] nenhum arquivo foi salvo.", tmdbMovie.getImdb_id());
            System.out.println(messsage);
        } else{
            Stream streamOrigin = result.getVideoSourceSreaming().getSourceStream();
            String streamName = streamOrigin.getStreamName();
            String movieTitle = streamOrigin.getMovieTitle();
            String downloadPathStr = result.getDownloadPath().toString();
            String message = String.format("[%s - Download concluido] [%s] arquivo salvo em:\n%s", movieTitle, streamName, downloadPathStr);
            System.out.println(message);
        }
       
    }
}

```

**Observação:** O conteudo que será baixado é uma playlist **m3u8** que depende de internet para ser carregado.


# Donwload de serie

```java

import java.nio.file.Paths;

import com.github.jonascaetanosz.embedmovies.tv.DownloadTv;
import com.github.jonascaetanosz.embedmovies.tv.models.DownloadResult;
import com.github.jonascaetanosz.embedmovies.tv.models.Tv;
import com.github.jonascaetanosz.embedmovies.tv.models.Stream;
import com.github.jonascaetanosz.embedmovies.tmdb.DetailsTv;
import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;

import java.nio.file.Path;


public class DownloadTvTest {
    public static void main(String[] args) {


        String tmdb_api_key = "sua-chave-de-api-tmdb-aqui";
        TmdbConfig.setApiKey(tmdb_api_key);
        String tmdbID = "93740";
        String episodeNumber = "1";
        String seasonNumber = "2";
        Tv tmdbTv = DetailsTv.details( tmdbID );
        String safeTitle = tmdbTv.getName().replaceAll("[\\\\/:*?\"<>|]", "_");
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads");
        Path tvDir = downloadsPath.resolve( String.format("%s/temporada_%s", safeTitle, seasonNumber) );
        DownloadResult result = DownloadTv.startDownload(tmdbID, tvDir, seasonNumber, episodeNumber);
        if (result == null){
            String messsage = String.format("[%s - download falhou] nenhum arquivo foi salvo.", tmdbTv.getId());
            System.out.println(messsage);
        } else{
            Stream streamOrigin = result.getVideoSourceSreaming().getSourceStream();
            String streamName = streamOrigin.getStreamName();
            String episodeTitle = streamOrigin.getEpisodeTitle();
            String downloadPathStr = result.getDownloadPath().toString();
            String message = String.format("[%s - Download concluido] [%s] arquivo salvo em:\n%s", episodeTitle, streamName, downloadPathStr);
            System.out.println(message);
        }
       
    }
}

```

**Observação:** O conteudo que será baixado é uma playlist **m3u8** que depende de internet para ser carregado.

## 🔎 Visão geral das classes

- **com.github.jonascaetanosz.embedmovies.tmdb.DetailsMovie** — Classe responsável por obter detalhes de filmes no TMDB.

- **com.github.jonascaetanosz.embedmovies.tmdb.DetailsTv** — Classe responsável por obter detalhes de séries no TMDB.

- **com.github.jonascaetanosz.embedmovies.tmdb.DetailsTvEpisodes** — Classe responsável por obter detalhes de episódios de séries por temporada no TMDB.

- **com.github.jonascaetanosz.embedmovies.tmdb.DetailsTvSeasons** — Classe responsável por obter detalhes de temporadas de séries no TMDB.

- **com.github.jonascaetanosz.embedmovies.tmdb.Search** — Classe responsável por realizar buscas por filmes e séries no TMDB.

- **com.github.jonascaetanosz.embedmovies.tmdb.Trending** — Classe responsável por buscar conteúdos em alta no TMDB.

- **com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig** — Classe responsável pelas configurações da API do TMDB.

- **com.github.jonascaetanosz.embedmovies.EmbedMoviesConfig** — Classe responsável pelas configurações de URLs e headers utilizados nas requisições às APIs.

- **com.github.jonascaetanosz.embedmovies.filmes.streamsAvailables** — Classe responsável por obter streams disponíveis para download de filmes.

- **com.github.jonascaetanosz.embedmovies.filmes.DownloadMovie** — Classe responsável pelo download de filmes.

- **com.github.jonascaetanosz.embedmovies.tv.streamsAvailables** — Classe responsável por obter streams disponíveis para download de séries.

- **com.github.jonascaetanosz.embedmovies.filmes.DownloadTv** — Classe responsável pelo download de séries.


## 🤝 Contribuições

Toda ajuda é **muito bem-vinda**!  
Se você deseja melhorar o projeto, sugerir novas funcionalidades, corrigir problemas ou enviar pull requests, fique totalmente à vontade para contribuir.

Obrigado por apoiar o desenvolvimento! 🚀

## Contato

Se você tiver alguma dúvida ou precisar de suporte, sinta-se à vontade para entrar em contato:

Jonas - jonascaetanodesouzajfgh@gmail.com

# 📄 Licença

distribuído sobre [MIT](https://choosealicense.com/licenses/mit/) Licença
