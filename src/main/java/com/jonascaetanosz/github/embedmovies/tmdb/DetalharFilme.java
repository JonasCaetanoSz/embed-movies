package com.jonascaetanosz.github.embedmovies.tmdb;

import com.jonascaetanosz.github.embedmovies.filmes.models.Filme;

import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetalharFilme {

    public static Filme detalhar(String tmdbID){
        String apiKey = TmdbConfig.getApiKey();
        URL baseUrl = null;
        URL urlFinal = null;
        Filme movie = null;

        // montar a URL para API tmdb

        try{
            baseUrl = TmdbConfig.getApiBaseUrl();
            String apiEndpoint = String.format("/3/movie/%s?language=pt-BR&api_key=%s", tmdbID, apiKey);
            urlFinal = baseUrl.toURI().resolve(apiEndpoint).toURL();
        
        } catch (MalformedURLException | URISyntaxException e){
            System.err.println("ERRO AO CRIAR URL TMDB:" + e.getMessage());
        }

        // fazer a requisição para a API tmdb
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlFinal).build();
            Gson gson = new Gson();
            Response response = client.newCall(request).execute();
            String json_string = response.body().string();
            movie =  gson.fromJson(json_string, Filme.class);
            return movie;

        } catch(IOException | JsonSyntaxException| NullPointerException e){
            System.err.println("PEGAR DADOS DO FILME ERRO:" + e);
        }
        return null;

}}