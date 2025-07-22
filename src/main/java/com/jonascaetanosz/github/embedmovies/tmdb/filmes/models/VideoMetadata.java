package com.jonascaetanosz.github.embedmovies.tmdb.filmes.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class VideoMetadata {
    private boolean hls;
    private String videoImage;
    private String videoSource;
    private String securedLink;
    private List<String> downloadLinks;
    private List<String> attachmentLinks;
    private String ck;

    public boolean isHls() {
        return hls;
    }

    public URL getVideoImage() {
        return safeToURL(videoImage);
    }

    public URL getVideoSource() {
        return safeToURL(videoSource);
    }

    public URL getSecuredLink() {
        return safeToURL(securedLink);
    }

    public List<String> getDownloadLinks() {
        return downloadLinks;
    }

    public List<String> getAttachmentLinks() {
        return attachmentLinks;
    }

    public String getCk() {
        return ck;
    }

    private URL safeToURL(String urlStr) {
        if (urlStr == null || urlStr.isEmpty()) {
            return null;
        }
        try {
            return new URI(urlStr).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            // Pode fazer log aqui, se quiser
            return null;
        }
    }
}
