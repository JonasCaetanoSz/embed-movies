package com.jonascaetanosz.github.embedmovies.filmes.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;

public class Stream {
    private String streamDescription;
    private String streamName;
    private String movieTitle;
    private String streamUrl;

    public Stream(String movieTitle, String streamDescription, String streamName, String streamUrl) {
        this.movieTitle = movieTitle;
        this.streamDescription = streamDescription;
        this.streamName = streamName;
        this.streamUrl = streamUrl;
    }

    public URL getStreamUrl() {
        try {
            return new URI(this.streamUrl).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            System.out.println("ERRO CRIAR LINK DO PLAYER: " + e.getMessage());
        }
        return null;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getStreamDescription() {
        return streamDescription;
    }

    public String getStreamName() {
        return streamName;
    }

}