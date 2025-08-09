package com.github.jonascaetanosz.embedmovies.tv.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;

public class Stream {
    private String streamDescription;
    private String streamName;
    private String episodeTitle;
    private String streamUrl;

    public Stream(String episodeTitle, String streamDescription, String streamName, String streamUrl) {
        this.episodeTitle = episodeTitle;
        this.streamDescription = streamDescription;
        this.streamName = streamName;
        this.streamUrl = streamUrl;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }
    
    public URL getStreamUrl() {
        try {
            return new URI(this.streamUrl).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            System.out.println("ERRO CRIAR LINK DO PLAYER SERIES: " + e.getMessage());
        }
        return null;
    }

    public String getStreamDescription() {
        return streamDescription;
    }

    public String getStreamName() {
        return streamName;
    }

}