package com.github.jonascaetanosz.embedmovies.tmdb.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.List;

import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;


public class Media {

    private boolean adult;
    private String backdrop_path;
    private String id;
    private String title;
    private String name;
    private String original_title;
    private String overview;
    private String poster_path;
    private String media_type;
    private String original_language;
    private List<Integer> genre_ids;
    private double popularity;
    private String release_date;
    private boolean video;
    private double vote_average;
    private int vote_count;

    public boolean isAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getRelease_date() {
        return release_date;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }
    
    public String getName() {
        return (this.name != null) ? this.name : this.title;
    }

    public URL getPoster_url() {
        try{
            URL base_Url = TmdbConfig.getMedia_Base_Url();
            URL finaUrl = base_Url.toURI().resolve( this.getPoster_path().substring(1) ).toURL();
            return finaUrl;
    } catch (MalformedURLException | URISyntaxException | NullPointerException e ){
        System.err.println("URL IMAGEM POSTER ERRO:" + e.getMessage());
        } 
    return null;

    }
}