
import com.jonascaetanosz.github.embedmovies.filmes.models.DownloadResult;
import com.jonascaetanosz.github.embedmovies.filmes.models.Stream;
import com.jonascaetanosz.github.embedmovies.filmes.models.Movie;
import com.jonascaetanosz.github.embedmovies.filmes.DownloadMovie;
import com.jonascaetanosz.github.embedmovies.tmdb.TmdbConfig;

import com.jonascaetanosz.github.embedmovies.tmdb.DetailsMovie;

import utils.LoadConfigs;

import java.nio.file.Paths;
import java.nio.file.Path;


public class DownloadMovieTest {
    public static void main(String[] args) {


        String tmdb_api_key = LoadConfigs.load("config_test.ini", "tmdb", "api_key");
        TmdbConfig.setApiKey(tmdb_api_key);
        String tmdbID = "862";
        Movie tmdbMovie = DetailsMovie.details( tmdbID );
        String safeTitle = tmdbMovie.getTitle().replaceAll("[\\\\/:*?\"<>|]", "_");
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads");
        Path movieDir = downloadsPath.resolve(safeTitle);
        DownloadResult result = DownloadMovie.startDownload(tmdbID, movieDir);
        if (result == null){
            String messsage = String.format("[%s - download falhou] nenhum arquivo foi salvo.");
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
