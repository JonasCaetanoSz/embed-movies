package com.jonascaetanosz.github.embedmovies.filmes;

import com.jonascaetanosz.github.embedmovies.filmes.models.VideoMetadata;
import com.jonascaetanosz.github.embedmovies.filmes.models.DownloadResult;
import com.jonascaetanosz.github.embedmovies.filmes.models.Player;
import com.jonascaetanosz.github.embedmovies.embedMoviesConfig;
import com.jonascaetanosz.github.embedmovies.filmes.utils.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import okhttp3.OkHttpClient;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.Request;

import java.util.List;
import java.util.Map;

import java.net.URL;

public class BaixarFilme {

    public static DownloadResult baixarFilme(String tmdbID, Path outputFolder) {

        List<Player> players = PlayerDisponiveis.players(tmdbID);
        VideoMetadata videoMetadata = null;

        for (Player player : players) {

            // pegar VideoSource do video de player Werezcdn

            if (player.getPlayerName().startsWith("Warezcnd")){
                videoMetadata = PlayerWarezcndVideoMetadata.getVideoMetadata(player);
            }
             // pegar VideoSource do video de player Vip 

            if ( player.getPlayerName().startsWith( "VIP") ){
                videoMetadata = PlayerVipVideoMetadata.getVideoMetadata(player);
            }
             // pegar VideoSource do video de player Premium 

            if ( player.getPlayerName().startsWith( "Premium") ){
                videoMetadata = PlayerPremiumVideoMetadata.getVideoMetadata(player);
            }
            
            // parar o loop quando encontrar videoSource.txt

            if (videoMetadata != null){
                break;
            }

        }   
        // baixar os arquivos do filme

        if (videoMetadata != null){

           try {
                Files.createDirectories(outputFolder);
                Path sourceFilePath = outputFolder.resolve("playlist.m3u8");
                Map<String , String> headersMap = embedMoviesConfig.getHeaders("videoSource"); 
                Headers headers = Headers.of(headersMap);
                URL videoSourceUrl = videoMetadata.getVideoSource();
                OkHttpClient client = new OkHttpClient();
                Request reqBuilder = new Request.Builder().url(videoSourceUrl).headers(headers).build();
                Response response = client.newCall(reqBuilder).execute();
                String responseContent = response.body().string();
                Files.writeString(sourceFilePath, responseContent );
                DownloadResult result = new DownloadResult(sourceFilePath, videoMetadata);
                return result;

           } catch (IOException e) {
                System.err.println("Erro ao baixar filme:" + e.getMessage());
           }
        }
    return null;

    }
    
}
