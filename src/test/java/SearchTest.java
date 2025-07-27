import java.util.List;

import com.github.jonascaetanosz.embedmovies.tmdb.Search;
import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;
import com.github.jonascaetanosz.embedmovies.tmdb.models.Media;

import utils.LoadConfigs;

public class SearchTest {
    public static void main(String[] args) {
        String tmdb_api_key = LoadConfigs.load("config_test.ini", "tmdb", "api_key");
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
