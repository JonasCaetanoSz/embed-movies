package com.github.jonascaetanosz.embedmovies.tv;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

import org.jsoup.select.Elements;

import com.github.jonascaetanosz.embedmovies.embedMoviesConfig;
import com.github.jonascaetanosz.embedmovies.tv.models.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import java.io.IOException;

import java.net.URI;
import java.net.URL;

public class streamsAvailables {
    
    public static List<Stream> getStreams(String tmdbID, String season, String episode ) {
        try {

            URL baseUrl = embedMoviesConfig.getUrl("players");
            String apiEndpoint = String.format("?id=%s&type=tv&season=%s&episode=%s", tmdbID, season, episode);
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
                    System.out.println("ERRO AO PROCESSAR PLAYERS SERIES: " + e.getMessage()) ;
                }
            }
            String responseContent = response.body().string();
            List<Stream> streams = new ArrayList<>();
            Document document = Jsoup.parse( responseContent );
            String episodeTitle = document.select(".info-text").text();

            Elements buttonsPlayer = document.select("button.hostDub");

            if (buttonsPlayer.size() == 0){
                buttonsPlayer = document.select("button.hostLeg");
            }
            
            for (Element button : buttonsPlayer){
                String streamName = button.selectFirst(".player-name").text();
                String streamDescription = button.selectFirst(".player-description").text();
                String onclick = button.attr("onclick");
                String streamUrl = extractUrlFromOnClick(onclick);
                Stream stream = new Stream(episodeTitle, streamDescription, streamName, streamUrl);
                streams.add(stream);
                
            }
            return streams;

        } catch (MalformedURLException | URISyntaxException e) {
            System.err.println("ERRO A MONTAR URL PLAYERS BASE SERIES:" + e.getMessage());
        } catch (IOException e){
            System.err.println("ERRO AO PEGAR PLAYERS SERIES:" + e.getMessage());
    }
    return null;
}
    private static String extractUrlFromOnClick(String onclick) {
        int start = onclick.indexOf("\"") + 1;
        int end = onclick.indexOf("\"", start);
        if (start >= 0 && end > start) {
            return onclick.substring(start, end);
        }
        return "URL não encontrada";
    }
}

