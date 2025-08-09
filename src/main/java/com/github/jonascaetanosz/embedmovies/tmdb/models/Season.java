package com.github.jonascaetanosz.embedmovies.tmdb.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.github.jonascaetanosz.embedmovies.tmdb.TmdbConfig;
import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("air_date")
    private String airDate;

    @SerializedName("episode_count")
    private int episodeCount;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("season_number")
    private int seasonNumber;

    @SerializedName("vote_average")
    private double voteAverage;

    public String getAirDate() {
        return airDate;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public double getVoteAverage() {
        return voteAverage;
    }


    public URL gettPosterUrl() {
        try{
            URL base_Url = TmdbConfig.getMedia_Base_Url();
            URL finaUrl = base_Url.toURI().resolve(this.getPosterPath().substring(1)).toURL();
            return finaUrl;
    } catch (MalformedURLException | URISyntaxException | NullPointerException e ){
        System.err.println("URL IMAGEM POSTER SERIES ERRO:" + e.getMessage());
        }
    return null;
    }

}
