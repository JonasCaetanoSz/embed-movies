package com.github.jonascaetanosz.embedmovies.tv.streams;

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

import com.github.jonascaetanosz.embedmovies.embedMoviesConfig;
import com.github.jonascaetanosz.embedmovies.tv.models.PremiumResponseData;
import com.github.jonascaetanosz.embedmovies.tv.models.Stream;
import com.github.jonascaetanosz.embedmovies.tv.models.Streaming;

import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

public class StreamPremium {

    public static Streaming getStreaming(Stream stream) {

    Streaming videoSourceSreaming = null;

    try {
        Map<String, String> headersMap = embedMoviesConfig.getHeaders("Premium");
        Headers headers = Headers.of(headersMap);
        OkHttpClient client = new OkHttpClient();

        String videoApiHostUrl;
        String videoHash;
        Pattern pattern = Pattern.compile("https?:\\/\\/([^\\/]+)\\/video\\/([a-f0-9]+)");
        Matcher matcher = pattern.matcher( stream.getStreamUrl().toString() );

        Request reqBuilder = new Request.Builder().url( stream.getStreamUrl() ).headers(headers).build();
        Response response = client.newCall(reqBuilder).execute();

        List<String> setCookies = response.headers("Set-Cookie");
        StringBuilder cookieBuilder = new StringBuilder();
        for (String cookie : setCookies) {
            String[] parts = cookie.split(";", 2);
            cookieBuilder.append(parts[0]).append("; ");
        }
        String cookieHeader = cookieBuilder.toString().trim();
    
        if (!matcher.find()){

                String responseContent = response.body().string();
                pattern = Pattern.compile("data-id\\s*=\\s*\"(\\d+)\"");
                matcher = pattern.matcher(responseContent);
                matcher.find();
                String videoID = matcher.group(1);
                
                URL ApiUrlFinal = embedMoviesConfig.getUrl("embed_premium_get_play");
                headersMap = embedMoviesConfig.getHeaders("Premium_api_get_play");
                headersMap.put("Cookie", cookieHeader); // Adiciona cookies
                headers = Headers.of(headersMap);

                String bodyData = String.format("action=%s&video_id=%s", "getPlayer", videoID);
                RequestBody body = RequestBody.create(bodyData, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"));
                reqBuilder = new Request.Builder().url(ApiUrlFinal).post(body).headers(headers).build();
                response = client.newCall(reqBuilder).execute();
                
                responseContent = response.body().string();
                Gson gson = new Gson();
                PremiumResponseData responseData = gson.fromJson(responseContent, PremiumResponseData.class);

                pattern = Pattern.compile("https?:\\/\\/([^\\/]+)\\/video\\/([a-f0-9]+)");
                matcher = pattern.matcher( responseData.data.video_url );
                matcher.find();

                videoApiHostUrl = matcher.group(1);
                videoHash = matcher.group(2);
        
            } else {

                videoApiHostUrl = matcher.group(1);
                videoHash = matcher.group(2);

        }
        
        String apiEndPoint = String.format("/player/index.php?data=%s&do=getVideo", videoHash);
        URL finalUrl = new URI("https://" + videoApiHostUrl + apiEndPoint).toURL();

        headersMap = embedMoviesConfig.getHeaders("Premium_api_get_video");
        headersMap.put("Cookie", cookieHeader);
        headers = Headers.of(headersMap);

        String bodyData = "hash=" + videoHash + "&r=" + URLEncoder.encode("https://" + videoApiHostUrl, StandardCharsets.UTF_8);
        RequestBody body = RequestBody.create(bodyData, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"));
        reqBuilder = new Request.Builder().url( finalUrl ).headers( headers ).post( body ).build();
        
        Gson gson = new Gson();
        response = client.newCall( reqBuilder ).execute();
        String responseContent = response.body().string();
        videoSourceSreaming = gson.fromJson( responseContent , Streaming.class );
        videoSourceSreaming.setSourceStream( stream );

        reqBuilder = new Request.Builder().url(videoSourceSreaming.getVideoSource()).headers(headers).build();
        response = client.newCall(reqBuilder).execute();

        if (response.code() == 200){
            return videoSourceSreaming;
        } else {
            throw new Exception("video não encontrado status " + response.code() );
        }


    } catch (IOException | URISyntaxException | IllegalStateException | IndexOutOfBoundsException | JsonSyntaxException  e) {
        System.out.println( "Erro ao procesar series player Premium: " + e.getMessage() );
        
    } catch (Exception e){
            System.err.println("Erro ao processar series player Premium: " + e.getMessage() );
        }


    return null;
}

}
