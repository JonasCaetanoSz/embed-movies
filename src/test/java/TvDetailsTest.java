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
