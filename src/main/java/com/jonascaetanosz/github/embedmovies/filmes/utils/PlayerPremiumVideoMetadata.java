package com.jonascaetanosz.github.embedmovies.filmes.utils;

import com.jonascaetanosz.github.embedmovies.filmes.models.PremiumResponseData;
import com.jonascaetanosz.github.embedmovies.filmes.models.VideoMetadata;
import com.jonascaetanosz.github.embedmovies.filmes.models.Player;
import com.jonascaetanosz.github.embedmovies.embedMoviesConfig;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.Headers;

import java.nio.charset.StandardCharsets;
import java.io.IOException;

import java.net.URISyntaxException;
import java.net.URLEncoder;


import java.net.URI;
import java.net.URL;

import java.util.regex.*;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class PlayerPremiumVideoMetadata {

    public static VideoMetadata getVideoMetadata(Player player) {

    VideoMetadata videoMetadata = null;

    try {
        Map<String, String> headersMap = embedMoviesConfig.getHeaders("Premium");
        Headers headers = Headers.of(headersMap);
        OkHttpClient client = new OkHttpClient();

        // 1. Extrair video hash e host do link do player

        String videoApiHostUrl;
        String videoHash;
        Pattern pattern = Pattern.compile("https?:\\/\\/([^\\/]+)\\/video\\/([a-f0-9]+)");
        Matcher matcher = pattern.matcher(player.getPlayerUrl().toString());

        // 2. Request GET no player pra obter cookies de sessão

        Request reqBuilder = new Request.Builder().url(player.getPlayerUrl()).headers(headers).build();
        Response response = client.newCall(reqBuilder).execute();

        // 3. Capturar cookies da resposta inicial

        List<String> setCookies = response.headers("Set-Cookie");
        StringBuilder cookieBuilder = new StringBuilder();
        for (String cookie : setCookies) {
            String[] parts = cookie.split(";", 2);
            cookieBuilder.append(parts[0]).append("; ");
        }
        String cookieHeader = cookieBuilder.toString().trim();
    
        if (!matcher.find()){

        // 4. se não encontrar video hash e host no player link então extrair o video Hash da pagina do player


                // Extrair o ID do vídeo

                String responseContent = response.body().string();
                pattern = Pattern.compile("data-id\\s*=\\s*\"(\\d+)\"");
                matcher = pattern.matcher(responseContent);
                matcher.find();
                System.out.println(responseContent);
                String videoID = matcher.group(1);

                // Segunda requisição (getPlayer)
                
                URL ApiUrlFinal = embedMoviesConfig.getUrl("embed_premium_get_play");
                headersMap = embedMoviesConfig.getHeaders("Premium_api_get_play");
                headersMap.put("Cookie", cookieHeader); // Adiciona cookies
                headers = Headers.of(headersMap);

                String bodyData = String.format("action=%s&video_id=%s", "getPlayer", videoID);
                RequestBody body = RequestBody.create(bodyData, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"));
                reqBuilder = new Request.Builder().url(ApiUrlFinal).post(body).headers(headers).build();
                response = client.newCall(reqBuilder).execute();

                // Parse JSON e extrair host + hash
                
                responseContent = response.body().string();
                Gson gson = new Gson();
                PremiumResponseData responseData = gson.fromJson(responseContent, PremiumResponseData.class);

                pattern = Pattern.compile("https?:\\/\\/([^\\/]+)\\/video\\/([a-f0-9]+)");
                matcher = pattern.matcher(responseData.data.video_url);
                matcher.find();

                videoApiHostUrl = matcher.group(1);
                videoHash = matcher.group(2);
        
            } else { // 5. encontrou video hash e host na url do player

                videoApiHostUrl = matcher.group(1);
                videoHash = matcher.group(2);

        }

        // 6. Terceira requisição (getVideo)
        
        String apiEndPoint = String.format("/player/index.php?data=%s&do=getVideo", videoHash);
        URL finalUrl = new URI("https://" + videoApiHostUrl + apiEndPoint).toURL();

        headersMap = embedMoviesConfig.getHeaders("Premium_api_get_video");
        headersMap.put("Cookie", cookieHeader); // Reutiliza os cookies
        headers = Headers.of(headersMap);

        String bodyData = "hash=" + videoHash + "&r=" + URLEncoder.encode("https://" + videoApiHostUrl, StandardCharsets.UTF_8);
        RequestBody body = RequestBody.create(bodyData, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"));
        reqBuilder = new Request.Builder().url(finalUrl).headers(headers).post(body).build();
        
        Gson gson = new Gson();
        response = client.newCall(reqBuilder).execute();
        String responseContent = response.body().string();
        videoMetadata = gson.fromJson(responseContent, VideoMetadata.class);


    } catch (IOException | IndexOutOfBoundsException | URISyntaxException e) {
        System.out.println("Erro ao procesar player Premium: " + e.getMessage());
    }

    return videoMetadata;
}

}
