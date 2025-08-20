
import utils.LoadConfigs;

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


        String tmdb_api_key = LoadConfigs.load("config_test.ini", "tmdb", "api_key");
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
