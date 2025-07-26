package com.jonascaetanosz.github.embedmovies.filmes.models;

import java.nio.file.Path;

public class DownloadResult {
    private Path downloadPath;
    private Streaming videoSourceSreaming; 

    public DownloadResult(Path downloadPath, Streaming videoSourceSreaming){
        this.downloadPath = downloadPath;
        this.videoSourceSreaming = videoSourceSreaming;
    }

    public Path getDownloadPath() {
        return this.downloadPath;
    }

    public Streaming getVideoSourceSreaming() {
        return videoSourceSreaming;
    }

}
