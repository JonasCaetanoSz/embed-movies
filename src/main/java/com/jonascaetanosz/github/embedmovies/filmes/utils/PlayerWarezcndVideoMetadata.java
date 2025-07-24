package com.jonascaetanosz.github.embedmovies.filmes.utils;

import com.jonascaetanosz.github.embedmovies.filmes.models.VideoMetadata;
import com.jonascaetanosz.github.embedmovies.filmes.models.warezcndData;
import com.jonascaetanosz.github.embedmovies.filmes.models.Player;

import com.jonascaetanosz.github.embedmovies.embedMoviesConfig;

import java.nio.charset.StandardCharsets;

import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URLEncoder;
import java.net.URI;
import java.net.URL;

import com.google.gson.Gson;


import java.util.regex.*;
import java.util.Map;

import okhttp3.*;


public class PlayerWarezcndVideoMetadata {
    
    public static VideoMetadata getVideoMetadata(Player player) {
    {
        OkHttpClient client = new OkHttpClient();
        VideoMetadata videoMetadata = null;
        Gson gson = new Gson();

        try {

            // 1. Requisição inicial (GET player URL)

            Map<String, String> headersMap = embedMoviesConfig.getHeaders("Warezcnd");
            String cookie = headersMap.containsKey("Cookie") ? headersMap.remove("Cookie") : "";
            Headers headers = Headers.of(headersMap);
            Request.Builder reqBuilder = new Request.Builder().url(player.getPlayerUrl()).headers(headers).get();
            if (!cookie.isEmpty()) reqBuilder.addHeader("Cookie", cookie);
            Response response = client.newCall(reqBuilder.build()).execute();

            String html = response.body().string(); // resultado

            // 2. Extrair dados JSON do HTML

            Matcher matcher = Pattern.compile("let data = '(\\[.*?\\])';", Pattern.DOTALL).matcher(html);
            matcher.find();

            warezcndData[] dados = gson.fromJson(matcher.group(1), warezcndData[].class);
            warezcndData item = dados[1];

            String sv = item.servers.split(",")[0];
            String id = item.id;
            String lang = item.audio;

            // 3. HEAD request getEmbed gerar headers de autorização

            URL embedUrl = new URI(embedMoviesConfig.getUrl("embed_warezcdn_get_embed") + String.format("?id=%s&sv=%s&lang=%s", id, sv, lang)).toURL();

            headersMap = embedMoviesConfig.getHeaders("Warezcnd_api_get_embed");
            headersMap.put("access-control-allow-origin", embedUrl.toString());
            cookie = headersMap.containsKey("Cookie") ? headersMap.remove("Cookie") : "";
            headers = Headers.of(headersMap);
            reqBuilder = new Request.Builder().url(embedUrl).headers(headers).head();
            if (!cookie.isEmpty()) reqBuilder.addHeader("Cookie", cookie);
            client.newCall(reqBuilder.build()).execute().close();

            // 4. GET request getPlay

            URL playUrl = new URI(embedMoviesConfig.getUrl("embed_warezcdn_get_play") + String.format("?id=%s&sv=%s", id, sv)).toURL();
            headersMap = embedMoviesConfig.getHeaders("Warezcnd_api_get_play");
            headersMap.put("Referer", embedUrl.toString());
            cookie = headersMap.containsKey("Cookie") ? headersMap.remove("Cookie") : "";
            headers = Headers.of(headersMap);
            reqBuilder = new Request.Builder().url(playUrl).headers(headers).get();
            if (!cookie.isEmpty()) reqBuilder.addHeader("Cookie", cookie);
            response = client.newCall(reqBuilder.build()).execute();

            String playHtml = response.body().string();

            // 5. Extrair link base e hash do video
            
            matcher = Pattern.compile("window\\.location\\.href\\s*=\\s*\"(https?://[^\"\\s]*/video/([a-fA-F0-9]+))").matcher(playHtml);
            matcher.find();

            String linkBase = matcher.group(1);
            String hash = matcher.group(2);

            // 6. GET request Player API que obtém o video source

            URL finalUrl = new URI(linkBase.split("/video/")[0] + "/player/index.php?data=" + hash + "&do=getVideo").toURL();
            String bodyData = "hash=" + hash + "&r=" + URLEncoder.encode("https://embed.warezcdn.link/", StandardCharsets.UTF_8);
            RequestBody body = RequestBody.create(bodyData, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"));
            headersMap = embedMoviesConfig.getHeaders("Warezcnd_api_get_video");
            headersMap.put("Referer", playUrl.toString());
            cookie = headersMap.containsKey("Cookie") ? headersMap.remove("Cookie") : "";
            headers = Headers.of(headersMap);
            reqBuilder = new Request.Builder().url(finalUrl).headers(headers).post(body);
            if (!cookie.isEmpty()) reqBuilder.addHeader("Cookie", cookie);
            response = client.newCall(reqBuilder.build()).execute();

            String jsonResponse = response.body().string();
            videoMetadata = gson.fromJson(jsonResponse, VideoMetadata.class);
            videoMetadata.setOriginPlayer(player);

        } catch (IOException | URISyntaxException | IllegalStateException e) {
            System.err.println("Erro ao processar player Warezcnd: " + e.getMessage());
        }
    
    return videoMetadata;

        }
    }
}
