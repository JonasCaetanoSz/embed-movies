package com.github.jonascaetanosz.embedmovies.tmdb.models;

import java.util.List;

public class Episode {
    private String air_date;
    private int episode_number;
    private String episode_type;
    private long id;
    private String name;
    private String overview;
    private String production_code;
    private int runtime;
    private int season_number;
    private long show_id;
    private String still_path;
    private double vote_average;
    private int vote_count;
    private List<CrewMember> crew;
    private List<GuestStar> guest_stars;

    public String getAir_date() { 
        return air_date; 
    }
    public int getEpisode_number() { 
        return episode_number; 
    }
    public String getEpisode_type() { 
        return episode_type; 
    }
    public long getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public String getOverview() { 
        return overview; 
    }
    public String getProduction_code() { 
        return production_code; 
    }
    public int getRuntime() { 
        return runtime; 
    }
    public int getSeason_number() { 
        return season_number; 
    }
    public long getShow_id() { 
        return show_id; 
    }
    public String getStill_path() { 
        return still_path; 
    }
    public double getVote_average() { 
        return vote_average;
    }
    public int getVote_count() { 
        return vote_count; 
    }
    public List<CrewMember> getCrew() { 
        return crew; 
    }
    public List<GuestStar> getGuest_stars() { 
        return guest_stars; 
    }
}
