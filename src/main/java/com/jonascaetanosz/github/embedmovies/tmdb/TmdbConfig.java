package com.jonascaetanosz.github.embedmovies.tmdb;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;

public class TmdbConfig {
    private static String api_key;
    private static URL BASE_URL;
    private static URL MEDIA_BASE_URL;
    
    static {
        try {

        TmdbConfig.BASE_URL = new URI("https://api.themoviedb.org").toURL();;
        TmdbConfig.MEDIA_BASE_URL = new URI("https://image.tmdb.org/t/p/w500").toURL();

        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("URL BASE INVALIDA:", e);
        }
    }

    public static void setApiKey(String api_key) {
        TmdbConfig.api_key = api_key;
    }

    public static String getApiKey(){
        if (api_key == null){
            throw new IllegalStateException("TOKEN DE API NÃO FOI CONFIGURADO");
        };

        return api_key;
    }

    public static URL getApiBaseUrl(){
        return TmdbConfig.BASE_URL;
    }

    public static URL getMedia_Base_Url() {
        return MEDIA_BASE_URL;
    }

}
