package com.github.jonascaetanosz.embedmovies.tmdb.models;

public class GuestStar {
    private String character;
    private String credit_id;
    private int order;
    private boolean adult;
    private int gender;
    private long id;
    private String known_for_department;
    private String name;
    private String original_name;
    private double popularity;
    private String profile_path;

    public String getCharacter() { 
        return character; 
    }
    public String getCredit_id() { 
        return credit_id; 
    }
    public int getOrder() { 
        return order; 
    }
    public boolean isAdult() { 
        return adult; 
    }
    public int getGender() { 
        return gender; 
    }
    public long getId() { 
        return id; 
    }
    public String getKnown_for_department() { 
        return known_for_department; 
    }
    public String getName() { 
        return name; 
    }
    public String getOriginal_name() { 
        return original_name; 
    }
    public double getPopularity() { 
        return popularity; 
    }
    public String getProfile_path() { 
        return profile_path; 
    }
}
