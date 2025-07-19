package com.jonascaetanosz.github.embedmovies.tmdb;

import java.net.MalformedURLException;
import java.net.URL;

public class TmdbConfig {
    private static String api_key;
    private static URL BASE_URL;
    private static URL MEDIA_BASE_URL;
    
    // Bloco de inicialização estático

    static {
        try {

        TmdbConfig.BASE_URL = new URL("https://api.themoviedb.org");
        TmdbConfig.MEDIA_BASE_URL = new URL("https://image.tmdb.org");

        } catch (MalformedURLException e) {
            throw new RuntimeException("URL BASE INVALIDA:", e);
        }
    }
    // setter para configurar a chave de API tmdb

    public static void setApiKey(String api_key) {
        TmdbConfig.api_key = api_key;
    }

    // getter para pegar a chave de API

    public static String getApiKey(){
        if (api_key == null){
            throw new IllegalStateException("TOKEN DE API NÃO FOI CONFIGURADO");
        };

        return api_key;
    }

    // getter para pegar o base_url da API

    public static URL getApiBaseUrl(){
        return TmdbConfig.BASE_URL;
    }

    // getter para pegar o poster url base do tmdb

    public static URL getMedia_Base_Url() {
        return MEDIA_BASE_URL;
    }

}
