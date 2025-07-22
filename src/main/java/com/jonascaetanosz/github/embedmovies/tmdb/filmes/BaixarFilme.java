package com.jonascaetanosz.github.embedmovies.tmdb.filmes;

import com.jonascaetanosz.github.embedmovies.tmdb.filmes.models.VideoMetadata;
import com.jonascaetanosz.github.embedmovies.tmdb.filmes.models.Player;
import com.jonascaetanosz.github.embedmovies.tmdb.filmes.utils.*;

import java.util.List;

// TODO: codigo provisorio testando VideMetadata de players

public class BaixarFilme {

    public static void baixarFilme(String tmdbID, String path) {

        List<Player> players = PlayerDisponiveis.players(tmdbID);
        VideoMetadata videoMetadata = null;

        for (Player player : players) {

            // pegar VideoSource do video

            if (player.getPlayerName().startsWith("Premium")) {

                videoMetadata = PlayerPremiumVideoMetadata.getVideoMetadata(player);
                
            }

            // parar o loop quando encontrar videoSource.txt

            if (videoMetadata != null){
                break;
            }

        }   // baixar playlist do video

            if (videoMetadata != null){
                System.out.println(videoMetadata.getVideoSource());
            }
    }
}
