import com.jonascaetanosz.github.embedmovies.tmdb.TmdbConfig;
import com.jonascaetanosz.github.embedmovies.tmdb.models.Media;

import java.util.List;

import com.jonascaetanosz.github.embedmovies.tmdb.Search;

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
