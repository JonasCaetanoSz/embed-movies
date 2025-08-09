import java.util.List;

import com.github.jonascaetanosz.embedmovies.tv.streamsAvailables;
import com.github.jonascaetanosz.embedmovies.tv.models.Stream;

public class TvStreamsAvailablesTest {
    public static void main(String[] args) {

        String tmdbID = "71446";
        String episodeNumber = "2";
        String seasonNumber = "1";
       List<Stream> streams =  streamsAvailables.getStreams(tmdbID,seasonNumber, episodeNumber);

       for (Stream stream : streams){
        System.err.println("\n---- player detalhes -----");
        System.out.println( String.format("transmissão link: %s", stream.getStreamUrl() ) );
        System.out.println( String.format("nome da transmissão: %s", stream.getStreamName() ) );
        System.out.println( String.format("descricao da transmissão: %s", stream.getStreamDescription() ) );
        System.out.println( String.format("titulo do episodio: %s", stream.getEpisodeTitle() ) );

       }
    }
}
