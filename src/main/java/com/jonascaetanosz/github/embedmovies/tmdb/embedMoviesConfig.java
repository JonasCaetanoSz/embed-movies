package com.jonascaetanosz.github.embedmovies.tmdb;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;

public class embedMoviesConfig {
    private static Map<String, Map<String, String>> headers;
    private static Map<String, String> urlsBase;

    // bloco de inicialização estatico

    static {
        try {

            // inicializar o Gson
            
            Gson gson = new Gson();

            // carregar links de API base

            InputStream urlsBaseStream = embedMoviesConfig.class.getClassLoader().getResourceAsStream("urlsBase/urls.json");
            Type typeUrls = new TypeToken<Map<String, String>>() {}.getType();
            urlsBase = gson.fromJson(new InputStreamReader(urlsBaseStream), typeUrls);
            urlsBaseStream.close();

            // carregar headers

            InputStream headersStream = embedMoviesConfig.class.getClassLoader().getResourceAsStream("headers/headers.json");
            Type typeHeaders = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
            headers = gson.fromJson(new InputStreamReader(headersStream), typeHeaders);
            headersStream.close();

        } catch (IOException e) {
            System.err.println("ERRO FECHAR O ARQUIVO:" + e.getMessage());
        }
    }

    // getter para pegar headers

    public static Map<String, String> getHeaders(String area){
        return headers.get(area);
    }

    // getter para pegar links base

    public static URL getUrl(String area){
        try {

        String urlString = urlsBase.get(area);
        URL urlBase = new URI(urlString).toURL();
        return urlBase;

    } catch (MalformedURLException | URISyntaxException e){
        System.err.println("ERRO CRIAR LINK DE API:" + e.getMessage());
    }
    return null;
    }
}
