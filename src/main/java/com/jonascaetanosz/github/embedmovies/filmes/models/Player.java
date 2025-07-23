package com.jonascaetanosz.github.embedmovies.filmes.models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;

public class Player {
    private String movietitle;
    private String playerDescription;
    private String playerName;
    private String playerUrl;

    // construtor com parâmetros

    public Player(String movietitle, String playerDescription, String playerName, String playerUrl) {
        this.movietitle = movietitle;
        this.playerDescription = playerDescription;
        this.playerName = playerName;
        this.playerUrl = playerUrl;
    }

    // Getters metodos

    public URL getPlayerUrl() {
        try {
            return new URI(this.playerUrl).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            System.out.println("ERRO CRIAR LINK DO PLAYER: " + e.getMessage());
        }
        return null;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerDescription() {
        return playerDescription;
    }

    public String getMovietitle() {
        return movietitle;
    }
}