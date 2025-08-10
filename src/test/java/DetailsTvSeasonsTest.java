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
