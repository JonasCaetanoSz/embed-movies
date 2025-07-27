package com.github.jonascaetanosz.embedmovies.tmdb;

import com.google.gson.JsonSyntaxException;
import com.github.jonascaetanosz.embedmovies.filmes.models.Movie;
import com.google.gson.Gson;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsMovie {

    public static Movie details(String tmdbID){
        String apiKey = TmdbConfig.getApiKey();
        URL baseUrl = null;
        URL urlFinal = null;
        Movie movie = null;

        try{
            baseUrl = TmdbConfig.getApiBaseUrl();
            String apiEndpoint = String.format("/3/movie/%s?language=pt-BR&api_key=%s", tmdbID, apiKey);
            urlFinal = baseUrl.toURI().resolve(apiEndpoint).toURL();
        
        } catch (MalformedURLException | URISyntaxException e){
            System.err.println("ERRO AO CRIAR URL TMDB:" + e.getMessage());
        }

        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlFinal).build();
            Gson gson = new Gson();
            Response response = client.newCall(request).execute();
            String json_string = response.body().string();
            movie =  gson.fromJson(json_string, Movie.class);
            return movie;

        } catch(IOException | JsonSyntaxException| NullPointerException e){
            System.err.println("PEGAR DADOS DO FILME ERRO:" + e);
        }
        return null;

}}