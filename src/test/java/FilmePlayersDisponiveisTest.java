import com.jonascaetanosz.github.embedmovies.tmdb.filmes.PlayerDisponiveis;
import com.jonascaetanosz.github.embedmovies.tmdb.filmes.models.Player;

import java.util.List;

public class FilmePlayersDisponiveisTest {
    public static void main(String[] args) {
       List<Player> players =  PlayerDisponiveis.players("35");

       for (Player player : players){
        System.err.println("\n---- player detalhes -----");
        System.out.println( String.format("nome do player: %s", player.getPlayerName() ) );
        System.out.println( String.format("descricao do player: %s", player.getPlayerDescription() ) );
        System.out.println( String.format("titulo do filme: %s", player.getMovietitle() ) );
        System.out.println( String.format("player link: %s", player.getPlayerUrl() ) );

       }
    }
}
