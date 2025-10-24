package com.github.jonascaetanosz.embedmovies.filmes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import okhttp3.OkHttpClient;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.Request;

import java.util.List;
import java.util.Map;

import com.github.jonascaetanosz.embedmovies.embedMoviesConfig;
import com.github.jonascaetanosz.embedmovies.filmes.models.DownloadResult;
import com.github.jonascaetanosz.embedmovies.filmes.models.Stream;
import com.github.jonascaetanosz.embedmovies.filmes.models.Streaming;
import com.github.jonascaetanosz.embedmovies.filmes.streams.*;

import java.net.URL;

public class DownloadMovie {

    public static DownloadResult startDownload(String tmdbID, Path outputFolder) {

        List<Stream> streams = streamsAvailables.getStreams(tmdbID);
        Streaming videoSourceSreaming = null;

        for (Stream stream : streams) {

            if (stream.getStreamName().startsWith( "Opção") ){
                videoSourceSreaming = GenericPlayer.getStreaming(stream);
            }

            if (stream.getStreamName().startsWith( "Warezcnd") ){
                videoSourceSreaming = StreamWarezcnd.getStreaming(stream);
            }

            if ( stream.getStreamName().startsWith( "VIP") ){
                videoSourceSreaming = StreamVip.getStreaming(stream);
            }

            if ( stream.getStreamName().startsWith( "Premium") ){
                videoSourceSreaming = StreamPremium.getStreaming(stream);
            }

            if ( (streams.indexOf(stream) + 1) == streams.size() & videoSourceSreaming == null ){
                Stream streamWarezcdnNacional = (Stream) streams.stream()
                    .filter(s -> stream.getStreamName().startsWith("Warezcnd"))
                    .findFirst()
                    .orElse(null);

                videoSourceSreaming = (streamWarezcdnNacional != null)
                    ? StreamWarezcndNacionalMovie.getStreaming(stream)
                    : null;
            }

            // stop loop if found streaming

            if (videoSourceSreaming != null){
                break;
            }

        }   
        // download

        if (videoSourceSreaming != null){

           try {
                Files.createDirectories(outputFolder);
                Path sourceFilePath = outputFolder.resolve("Filme.m3u8");
                Map<String , String> headersMap = embedMoviesConfig.getHeaders("videoSource"); 
                Headers headers = Headers.of( headersMap );
                URL videoSourceUrl = videoSourceSreaming.getVideoSource();
                OkHttpClient client = new OkHttpClient();
                Request reqBuilder = new Request.Builder().url( videoSourceUrl ).headers( headers ).build();
                Response response = client.newCall(reqBuilder).execute();
                String responseContent = response.body().string();
                Files.writeString(sourceFilePath, responseContent );
                DownloadResult result = new DownloadResult( sourceFilePath, videoSourceSreaming );
                return result;

           } catch (IOException e) {
                System.err.println("Erro ao baixar filme:" + e.getMessage());
           }
        }
    return null;

    }
    
}
