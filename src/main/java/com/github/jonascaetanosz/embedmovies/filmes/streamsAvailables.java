package com.github.jonascaetanosz.embedmovies.filmes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

import org.jsoup.select.Elements;

import com.github.jonascaetanosz.embedmovies.embedMoviesConfig;
import com.github.jonascaetanosz.embedmovies.filmes.models.PlayersApiResponse;
import com.github.jonascaetanosz.embedmovies.filmes.models.Stream;
import com.github.jonascaetanosz.embedmovies.filmes.models.PlayersApiResponse.Option;
import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import java.io.IOException;

import java.net.URI;
import java.net.URL;

public class streamsAvailables {
    
    public static List<Stream> getStreams(String tmdbID) {
        try {

            URL baseUrl = embedMoviesConfig.getUrl("players");
            String apiEndpoint = String.format("?type=movie&id=%s&season=null&episode=null", tmdbID);
            URL finaUrl = new URI(baseUrl.toString() + apiEndpoint).toURL();
            Map<String, String> headersMap = embedMoviesConfig.getHeaders("players");
            Headers headers = Headers.of(headersMap);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(finaUrl).headers(headers).build();
            Response response = null;

            for (int i = 0; i < 5; i++){
                try {
                    response = client.newCall(request).execute();
                    break;
                } catch (Exception e) {
                    System.out.println("ERRO AO PROCESSAR PLAYERS: " + e.getMessage()) ;
                }
            }

            String responseContent = response.body().string();

            if (responseContent == null) {

                System.out.println("ERRO AO PROCESSAR PLAYERS: [request body is null]" );
                return null;
            }

            List<Stream> streams = new ArrayList<>();
            
            Gson gson = new Gson();
            PlayersApiResponse playersResponseJson = gson.fromJson(responseContent, PlayersApiResponse.class);
            
            if (playersResponseJson.status() == false){

                System.out.println("ERRO AO PROCESSAR PLAYERS: " + String.format("[request status code: %s ]", response.code() ));
                return null;
            }

             String movieTitle = playersResponseJson.data().title();

            for (Option option : playersResponseJson.data().options() ) {

                if ( !option.lang().equalsIgnoreCase("pt-br")){
                    continue;
                }

                String streamDescription = null;

                if (option.budget().equalsIgnoreCase("success")) {

                    streamDescription = "sem anuncios";

                } else {
                    streamDescription = "com anuncios";
                }

                String streamName = option.label();
                String streamUrl = option.embed();
                Stream stream = new Stream(movieTitle, streamDescription, streamName, streamUrl);
                streams.add(stream);
            }

            return streams;

        } catch (MalformedURLException | URISyntaxException e) {
            System.err.println("ERRO A MONTAR URL PLAYERS BASE:" + e.getMessage());
        } catch (IOException e){
            System.err.println("ERRO AO PEGAR PLAYERS:" + e.getMessage());
    }
    return null;
    
}
}
