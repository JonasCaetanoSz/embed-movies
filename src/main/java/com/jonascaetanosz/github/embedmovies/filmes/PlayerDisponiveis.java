package com.jonascaetanosz.github.embedmovies.filmes;

import com.jonascaetanosz.github.embedmovies.filmes.models.Player;
import com.jonascaetanosz.github.embedmovies.embedMoviesConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import java.io.IOException;

import java.net.URI;
import java.net.URL;

public class PlayerDisponiveis {
    
    public static List<Player> players(String tmdbID) {
        try {
            // montar a requisição e enviar

            URL baseUrl = embedMoviesConfig.getUrl("players");
            String apiEndpoint = String.format("?id=%s&type=movie", tmdbID);
            URL finaUrl = new URI(baseUrl.toString() + apiEndpoint).toURL();
            Map<String, String> headersMap = embedMoviesConfig.getHeaders("players");
            Headers headers = Headers.of(headersMap);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(finaUrl).headers(headers).build();
            Response response = client.newCall(request).execute();
            String responseContent = response.body().string();

            // raspar os dados do html

            List<Player> playersResult = new ArrayList<>();
            Document document = Jsoup.parse( responseContent );
            String movieTitle = document.select(".info-text").text();

            Elements buttonsPlayer = document.select("button.player-btn");
            for (Element button : buttonsPlayer){
                String playerName = button.selectFirst(".player-name").text();
                String playerDescription = button.selectFirst(".player-description").text();
                String onclick = button.attr("onclick");
                String playerUrl = extractUrlFromOnClick(onclick);
                Player player = new Player(movieTitle, playerDescription, playerName, playerUrl);
                playersResult.add(player);
                
            }
            return playersResult;

        } catch (MalformedURLException | URISyntaxException e) {
            System.err.println("ERRO A MONTAR URL PLAYERS BASE:" + e.getMessage());
        } catch (IOException e){
            System.err.println("ERRO AO PEGAR PLAYERS:" + e.getMessage());
    }
    return null;
}

    // Extrai o link de dentro de: callPlayer("LINK_AQUI", ...)
    private static String extractUrlFromOnClick(String onclick) {
        int start = onclick.indexOf("\"") + 1;
        int end = onclick.indexOf("\"", start);
        if (start >= 0 && end > start) {
            return onclick.substring(start, end);
        }
        return "URL não encontrada";
    }
}

