package com.github.jonascaetanosz.embedmovies.tv.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;

public class ProductionCompany {
    private int id;
    private String logo_path;
    private String name;
    private String origin_country;

    public int getId() { return id; }
    public String getLogo_path() { return logo_path; }
    public String getName() { return name; }
    public String getOrigin_country() { return origin_country; }

    public URL getLogo_url() {
        try{
            URL base_Url = TmdbConfig.getMedia_Base_Url();
            URL finaUrl = base_Url.toURI().resolve(this.getLogo_path().substring(1)).toURL();
            return finaUrl;
    } catch (MalformedURLException | URISyntaxException | NullPointerException  e ){
        System.err.println("URL IMAGEM EMPRESA ERRO:" + e.getMessage());
        }
    return null;
    }
}
