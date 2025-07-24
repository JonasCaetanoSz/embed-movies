
import com.jonascaetanosz.github.embedmovies.filmes.models.DownloadResult;
import com.jonascaetanosz.github.embedmovies.filmes.models.Player;
import com.jonascaetanosz.github.embedmovies.filmes.models.Filme;
import com.jonascaetanosz.github.embedmovies.filmes.BaixarFilme;
import com.jonascaetanosz.github.embedmovies.tmdb.TmdbConfig;

import com.jonascaetanosz.github.embedmovies.tmdb.DetalharFilme;

import utils.CarregarConfig;

import java.nio.file.Paths;
import java.nio.file.Path;


public class baixarFilmeTest {
    public static void main(String[] args) {


        String tmdb_api_key = CarregarConfig.carregar("config_test.ini", "tmdb", "api_key");
        TmdbConfig.setApiKey(tmdb_api_key);
        String tmdbID = "862";
        Filme tmdbMovie = DetalharFilme.detalhar( tmdbID );
        String safeTitle = tmdbMovie.getTitle().replaceAll("[\\\\/:*?\"<>|]", "_");
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads");
        Path movieDir = downloadsPath.resolve(safeTitle);
        DownloadResult result = BaixarFilme.baixarFilme(tmdbID, movieDir);
        if (result == null){
            String messsage = String.format("[%s - download falhou] nenhum arquivo foi salvo.");
            System.out.println(messsage);
        } else{
            Player playerOrigin = result.getVideoMetadata().getOriginPlayer();
            String playerName = playerOrigin.getPlayerName();
            String movieTitle = playerOrigin.getMovietitle();
            String downloadPathStr = result.getDownloadPath().toString();
            String message = String.format("[%s - Download concluido] [%s] arquivo salvo em:\n%s", movieTitle, playerName, downloadPathStr);
            System.out.println(message);
        }
       
    }
}
