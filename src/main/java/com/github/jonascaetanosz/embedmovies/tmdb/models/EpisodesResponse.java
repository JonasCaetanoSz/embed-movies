package com.github.jonascaetanosz.embedmovies.tmdb.models;

import java.util.List;

public class EpisodesResponse {
    private String _id;
    private String air_date;
    private List<Episode> episodes;
    private String name;
    private String overview;
    private int id;
    private String poster_path;
    private int season_number;
    private double vote_average;

    public String get_id() { 
        return _id;
    }
    public String getAir_date() { 
        return air_date; 
    }
    public List<Episode> getEpisodes() { 
        return episodes; 
    }
    public String getName() { 
        return name; 
    }
    public String getOverview() { 
        return overview; 
    }
    public int getId() { 
        return id; 
    }
    public String getPoster_path() { 
        return poster_path; 
    }
    public int getSeason_number() { 
        return season_number; 
    }
    public double getVote_average() { 
        return vote_average; 
    }
}
