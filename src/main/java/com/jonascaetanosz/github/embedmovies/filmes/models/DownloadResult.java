package com.jonascaetanosz.github.embedmovies.filmes.models;

import java.nio.file.Path;

public class DownloadResult {
    private Path downloadPath;
    private VideoMetadata videoMetadata; 

    public DownloadResult(Path downloadPath, VideoMetadata videoMetadata){
        this.downloadPath = downloadPath;
        this.videoMetadata = videoMetadata;
    }

    public Path getDownloadPath() {
        return this.downloadPath;
    }
    
    public VideoMetadata getVideoMetadata() {
        return videoMetadata;
    }

}
