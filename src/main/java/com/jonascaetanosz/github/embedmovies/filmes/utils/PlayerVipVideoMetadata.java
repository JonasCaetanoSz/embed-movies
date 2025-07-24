package com.jonascaetanosz.github.embedmovies.filmes.utils;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Headers;
import okhttp3.MediaType;

import com.jonascaetanosz.github.embedmovies.filmes.models.VideoMetadata;
import com.google.gson.Gson;
import com.jonascaetanosz.github.embedmovies.embedMoviesConfig;
import com.jonascaetanosz.github.embedmovies.filmes.models.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class PlayerVipVideoMetadata {

    public static VideoMetadata getVideoMetadata (Player player){
        VideoMetadata videoMetadata = null;

        try{
            Map<String, String> headersMap = embedMoviesConfig.getHeaders("Vip_player");
            Headers headers = Headers.of(headersMap);
            OkHttpClient client = new OkHttpClient();

        // 1. Extrair video hash e host do link do player

            Pattern pattern = Pattern.compile("https?:\\/\\/([^\\/]+)\\/video\\/([a-f0-9]+)");
            Matcher matcher = pattern.matcher(player.getPlayerUrl().toString());
            matcher.find();
            String videoApiHostUrl = matcher.group(1);
            String videoHash = matcher.group(2);

        // 2. GET request gerar cookies de sessão
        
        Request reqBuilder = new Request.Builder().url( player.getPlayerUrl() ).headers(headers).build();
        Response response = client.newCall(reqBuilder).execute();


        // 3. Capturar cookies da resposta inicial

        List<String> setCookies = response.headers("Set-Cookie");
        StringBuilder cookieBuilder = new StringBuilder();
        for (String cookie : setCookies) {
            String[] parts = cookie.split(";", 2);
            cookieBuilder.append(parts[0]).append("; ");
        }
        String cookieHeader = cookieBuilder.toString().trim();

        // 4. GET request (GetVideo)
        
        String apiEndPoint = String.format("/player/index.php?data=%s&do=getVideo", videoHash);
        URL finalUrl = new URI("https://" + videoApiHostUrl + apiEndPoint).toURL();

        headersMap = embedMoviesConfig.getHeaders("Vip_player_api_get_video");
        headersMap.put("Cookie", cookieHeader); // Reutiliza os cookies
        headersMap.put("Origin", "https://" + videoApiHostUrl);
        headers = Headers.of(headersMap);

        String bodyData = "hash=" + videoHash + "&r=" + URLEncoder.encode("https://" + videoApiHostUrl, StandardCharsets.UTF_8);
        RequestBody body = RequestBody.create(bodyData, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"));
        reqBuilder = new Request.Builder().url(finalUrl).headers(headers).post(body).build();
        
        Gson gson = new Gson();
        response = client.newCall(reqBuilder).execute();
        String responseContent = response.body().string();
        videoMetadata = gson.fromJson(responseContent, VideoMetadata.class);
        videoMetadata.setOriginPlayer(player);
        
        } catch (IOException | URISyntaxException | IllegalStateException e){
            System.err.println("Erro ao processar player Vip:" + e.getMessage());
        }

        return videoMetadata;
    }

}
