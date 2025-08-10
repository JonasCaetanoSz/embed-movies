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
