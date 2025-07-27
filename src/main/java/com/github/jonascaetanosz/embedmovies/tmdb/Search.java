package com.github.jonascaetanosz.embedmovies.tmdb;

import com.github.jonascaetanosz.embedmovies.tmdb.models.Media;
import com.github.jonascaetanosz.embedmovies.tmdb.models.MediaResponse;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

import java.io.IOException;


import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;

import java.util.List;

public class Search {
    public static List<Media> search(String query){
        String apiKey = TmdbConfig.getApiKey();
        URL baseUrl = TmdbConfig.getApiBaseUrl();

        try {
            String querySecure = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String apiEndPoint = String.format("/3/search/multi?api_key=%s&query=%s&language=pt-BR", apiKey, querySecure);
            URL apiFinalUrl = new URI(baseUrl + apiEndPoint).toURL();
            OkHttpClient client = new OkHttpClient();
            Request reqBuilder = new Request.Builder().url(apiFinalUrl).build();
            Response response = client.newCall(reqBuilder).execute();
            String responseContent = response.body().string();

            Gson gson = new Gson();
            MediaResponse responseObj = gson.fromJson(responseContent, MediaResponse.class);
            List<Media> medias = responseObj.getResults();
            return medias;

        } catch( IOException | URISyntaxException e){
            System.err.println("ERRO BUSCA MEDIAS:" + e.getMessage());
        }
    return null;
    }

}