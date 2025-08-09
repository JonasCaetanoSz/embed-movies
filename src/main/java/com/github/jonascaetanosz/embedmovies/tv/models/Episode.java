package com.github.jonascaetanosz.embedmovies.tv.models;

public class Episode {
    private int id;
    private String name;
    private String overview;
    private double vote_average;
    private int vote_count;
    private String air_date;
    private int episode_number;
    private String episode_type;
    private String production_code;
    private Integer runtime;
    private int season_number;
    private int show_id;
    private String still_path;

    public int getId() {
        return id; 
    }
    public String getName() {
        return name; 
    }
    public String getOverview() {
        return overview; 
    }
    public double getVote_average() {
        return vote_average; 
    }
    public int getVote_count() { 
        return vote_count; 
    }
    public String getAir_date() { 
        return air_date; 
    }
    public int getEpisode_number() { 
        return episode_number; 
    }
    public String getEpisode_type() { 
        return episode_type; 
    }
    public String getProduction_code() { 
        return production_code; 
    }
    public Integer getRuntime() { 
        return runtime; 
    }
    public int getSeason_number() { 
        return season_number; 
    }
    public int getShow_id() { 
        return show_id; 
    }
    public String getStill_path() { 
        return still_path; 
    }
}
