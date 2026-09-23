package com.github.jonascaetanosz.embedmovies.filmes.models;

import java.util.List;

public record PlayersApiResponse(
        Boolean status,
        MovieData data
) {

    public record MovieData(
            Integer id,
            String title,
            String bgImage,
            String release_date,
            Integer seasons,
            Integer season_count,
            List<Option> options
    ) {}

    public record Option(
            String embed,
            String lang,
            String embed_id,
            String label,
            String budget,
            String icon
    ) {}
}