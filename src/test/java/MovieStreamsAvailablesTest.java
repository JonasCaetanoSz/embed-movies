import com.jonascaetanosz.github.embedmovies.filmes.streamsAvailables;
import com.jonascaetanosz.github.embedmovies.filmes.models.Stream;

import java.util.List;

public class MovieStreamsAvailablesTest {
    public static void main(String[] args) {
       List<Stream> streams =  streamsAvailables.getStreams("35");

       for (Stream stream : streams){
        System.err.println("\n---- player detalhes -----");
        System.out.println( String.format("transmissão link: %s", stream.getStreamUrl() ) );
        System.out.println( String.format("nome da transmissão: %s", stream.getStreamName() ) );
        System.out.println( String.format("descricao da transmissão: %s", stream.getStreamDescription() ) );
        System.out.println( String.format("titulo do filme: %s", stream.getMovieTitle() ) );

       }
    }
}
