package com.github.jonascaetanosz.embedmovies.filmes.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;

public class Movie {
    private String title;
    private String overview;
    private String imdb_id;
    private List<String> origin_country;
    private String original_language;
    private String original_title;
    private String popularity;
    private String poster_path;
    private List<ProductionCompanies> production_companies;
    private List<Genres> genres;
    private String status;
    private String tagline;
    private Float vote_average;
    private Float vote_count;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public List<ProductionCompanies> getProduction_companies() {
        return production_companies;
    }

        public List<Genres> getGenres() {
        return genres;
    }

     public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

   public Float getVote_average() {
        return vote_average;
    }

    public Float getVote_count() {
        return vote_count;
    }

    public URL getPoster_url() {
        try{
            URL base_Url = TmdbConfig.getMedia_Base_Url();
            URL finaUrl = base_Url.toURI().resolve(this.getPoster_path().substring(1)).toURL();
            return finaUrl;
    } catch (MalformedURLException | URISyntaxException e ){
        System.err.println("URL IMAGEM POSTER ERRO:" + e.getMessage());
        }
    return null;
    }

}
