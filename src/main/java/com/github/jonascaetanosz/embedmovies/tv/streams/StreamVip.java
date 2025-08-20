package com.github.jonascaetanosz.embedmovies.tv.streams;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.Headers;

import com.github.jonascaetanosz.embedmovies.embedMoviesConfig;
import com.github.jonascaetanosz.embedmovies.tv.models.Stream;
import com.github.jonascaetanosz.embedmovies.tv.models.Streaming;

import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Map;

import java.io.IOException;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.URI;
import java.net.URL;

public class StreamVip {

    public static Streaming getStreaming (Stream stream){
        Streaming videoSourceSreaming = null;

        try{
            Map<String, String> headersMap = embedMoviesConfig.getHeaders("Vip_player");
            Headers headers = Headers.of( headersMap );
            OkHttpClient client = new OkHttpClient();

            Pattern pattern = Pattern.compile("https?:\\/\\/([^\\/]+)\\/video\\/([a-f0-9]+)");
            Matcher matcher = pattern.matcher( stream.getStreamUrl().toString() );
            matcher.find();
            String videoApiHostUrl = matcher.group(1);
            String videoHash = matcher.group(2);
        
        Request reqBuilder = new Request.Builder().url( stream.getStreamUrl() ).headers(headers).build();
        Response response = client.newCall(reqBuilder).execute();

        List<String> setCookies = response.headers("Set-Cookie");
        StringBuilder cookieBuilder = new StringBuilder();
        for (String cookie : setCookies) {
            String[] parts = cookie.split(";", 2);
            cookieBuilder.append( parts[0] ).append("; ");
        }
        String cookieHeader = cookieBuilder.toString().trim(); 

        String apiEndPoint = String.format("/player/index.php?data=%s&do=getVideo", videoHash);
        URL finalUrl = new URI( "https://" + videoApiHostUrl + apiEndPoint ).toURL();

        headersMap = embedMoviesConfig.getHeaders("Vip_player_api_get_video");
        headersMap.put("Cookie", cookieHeader ); // Reutiliza os cookies
        headersMap.put("Origin", "https://" + videoApiHostUrl);
        headers = Headers.of( headersMap );

        String bodyData = "hash=" + videoHash + "&r=" + URLEncoder.encode( "https://" + videoApiHostUrl, StandardCharsets.UTF_8 );
        RequestBody body = RequestBody.create(bodyData, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"));
        reqBuilder = new Request.Builder().url(finalUrl).headers(headers).post(body).build();
        
        Gson gson = new Gson();
        response = client.newCall(reqBuilder).execute();
        String responseContent = response.body().string();
        videoSourceSreaming = gson.fromJson(responseContent, Streaming.class);
        videoSourceSreaming.setSourceStream( stream );
        
        } catch (IOException | URISyntaxException | IllegalStateException | IndexOutOfBoundsException| JsonSyntaxException  e){
            System.err.println("Erro ao processar series player Vip:" + e.getMessage());
        }

        return videoSourceSreaming;
    }

}
