package com.github.jonascaetanosz.embedmovies.tmdb;

import com.google.gson.JsonSyntaxException;
import com.github.jonascaetanosz.embedmovies.tmdb.models.Episode;
import com.github.jonascaetanosz.embedmovies.tmdb.models.EpisodesResponse;
import com.google.gson.Gson;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsTvEpisodes {

    public static List<Episode> details(String tmdbID, String season){
        String apiKey = TmdbConfig.getApiKey();
        URL baseUrl = null;
        URL urlFinal = null;
        List<Episode> episodes = null;
        EpisodesResponse responseObj = null;

        try{
            baseUrl = TmdbConfig.getApiBaseUrl();
            String apiEndpoint = String.format("/3/tv/%s/season/%s?language=pt-BR&api_key=%s", tmdbID, season, apiKey);
            urlFinal = baseUrl.toURI().resolve(apiEndpoint).toURL();
        
        } catch (MalformedURLException | URISyntaxException e){
            System.err.println("ERRO AO CRIAR URL TMDB EPISODIO:" + e.getMessage());
        }

        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlFinal).build();
            Gson gson = new Gson();
            Response response = client.newCall(request).execute();
            String json_string = response.body().string();
            responseObj =  gson.fromJson(json_string, EpisodesResponse.class);
            episodes = responseObj.getEpisodes();
            return episodes;

        } catch(IOException | JsonSyntaxException| NullPointerException e){
            System.err.println("PEGAR DADOS EPISODIO ERRO:" + e);
        }
        return null;

}}