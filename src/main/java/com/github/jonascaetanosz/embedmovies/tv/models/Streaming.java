package com.github.jonascaetanosz.embedmovies.tv.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class Streaming {
    private String videoImage;
    private String videoSource;
    private String securedLink;
    private Stream sourceStream;
    private List<String> downloadLinks;
    private List<String> attachmentLinks;
    private boolean hls;
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

            return null;
        }
    }
    
    public Stream getSourceStream() {
        return sourceStream;
    }

    public void setSourceStream(Stream sourceStream) {
        this.sourceStream = sourceStream;
    }

}
