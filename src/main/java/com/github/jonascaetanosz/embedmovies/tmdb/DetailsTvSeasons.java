package com.github.jonascaetanosz.embedmovies.tmdb;


import com.github.jonascaetanosz.embedmovies.tmdb.models.DetailsTvSeasonsResponse;
import com.github.jonascaetanosz.embedmovies.tmdb.models.Season;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;


public class DetailsTvSeasons {

    public static List<Season> getAllSeasons(String tmdbID){
        String apiKey = TmdbConfig.getApiKey();
        DetailsTvSeasonsResponse responseTV = null;
        URL baseUrl = null;
        URL urlFinal = null;
        List<Season> seasons = null;

        try{
            baseUrl = TmdbConfig.getApiBaseUrl();
            String apiEndpoint = String.format("/3/tv/%s?language=pt-BR&api_key=%s", tmdbID, apiKey);
            urlFinal = baseUrl.toURI().resolve(apiEndpoint).toURL();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlFinal).build();
            Response response = client.newCall(request).execute();
            String json_string = response.body().string();
            Gson gson = new Gson();
            responseTV =  gson.fromJson(json_string, DetailsTvSeasonsResponse.class);
            seasons = responseTV.getSeasons();

        } catch (MalformedURLException | URISyntaxException e){
            System.err.println("ERRO AO CRIAR URL TMDB TEMPORADAS:" + e.getMessage());

        } catch(IOException | IllegalStateException  e){
            System.err.println("ERRO EM TMDB TEMPORADAS:" + e.getMessage());
        }

        return seasons;
    }}