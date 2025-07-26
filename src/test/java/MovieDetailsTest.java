import com.jonascaetanosz.github.embedmovies.filmes.models.ProductionCompanies;
import com.jonascaetanosz.github.embedmovies.filmes.models.Movie;
import com.jonascaetanosz.github.embedmovies.tmdb.DetailsMovie;
import com.jonascaetanosz.github.embedmovies.tmdb.TmdbConfig;

import utils.LoadConfigs;

public class MovieDetailsTest {
    public static void main(String[] args) {
        String tmdb_api_key = LoadConfigs.load("config_test.ini", "tmdb", "api_key");
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
