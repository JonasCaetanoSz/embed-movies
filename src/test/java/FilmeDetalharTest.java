import com.jonascaetanosz.github.embedmovies.filmes.models.ProductionCompanies;
import com.jonascaetanosz.github.embedmovies.filmes.models.Filme;
import com.jonascaetanosz.github.embedmovies.tmdb.DetalharFilme;
import com.jonascaetanosz.github.embedmovies.tmdb.TmdbConfig;

import utils.CarregarConfig;

public class FilmeDetalharTest {
    public static void main(String[] args) {
        String tmdb_api_key = CarregarConfig.carregar("config_test.ini", "tmdb", "api_key");
        TmdbConfig.setApiKey(tmdb_api_key);
        Filme filme = DetalharFilme.detalhar("35");

        System.out.println( String.format( "\ntitulo: %s", filme.getTitle() ) );
        System.out.println( String.format( "\noverview: %s", filme.getOverview() ) );
        System.out.println( String.format( "\nimdb id: %s", filme.getImdb_id() ) );
        System.out.println( String.format( "\npais de origem: %s", filme.getOrigin_country() ) );
        System.out.println( String.format( "\noriginal idioma: %s", filme.getOriginal_language() ) );
        System.out.println( String.format( "\ntitulo original: %s", filme.getOriginal_title() ) );
        System.out.println( String.format( "\npopularidade: %s", filme.getPopularity() ) );
        System.out.println( String.format( "\nposter_path: %s", filme.getPoster_path() ) );
            System.out.println( String.format("poster url: %s", filme.getPoster_url() ) );

        System.out.println( "\n----empresas de produção:----\n" );

        for (ProductionCompanies company : filme.getProduction_companies()){
            System.out.println( String.format("nome: %s", company.getName() ) );
            System.out.println( String.format("ID: %s", company.getId() ) );
            System.out.println( String.format("logo path: %s", company.getLogo_path() ) );
            System.out.println( String.format("pais de origem: %s\n", company.getOrigin_country() ) );
            System.out.println( String.format("logo url: %s", company.getLogo_url() ) );

        }
        System.out.println("\n----fim empresas que produziu----\n");

        System.out.println( String.format( "\nstatus: %s", filme.getStatus() ) );
        System.out.println( String.format( "\ntagline: %s", filme.getTagline())  );
        System.out.println( String.format( "\nmedia de votação: %s", filme.getVote_average() ) );
        System.out.println( String.format( "\ncontagem de votos: %s", filme.getVote_count() ) );


    }
}
