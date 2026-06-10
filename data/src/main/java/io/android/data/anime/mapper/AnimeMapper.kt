package io.android.data.anime.mapper

import io.android.data.anime.model.AnimeDto
import io.android.domain.anime.model.Anime

/**
 * Mapeia o DTO (Firebase/Snake_Case) para o Domínio (Clean Architecture/CamelCase).
 * BMF: Mantemos a separação de responsabilidades entre as camadas.
 */
fun AnimeDto.toDomain(): Anime {
    return Anime(
        id = this.id,
        title = this.title,
        description = this.description,
        // Fazendo a ponte entre os nomes
        coverUrl = this.cover_url,
        bannerUrl = this.banner_url,
        tags = this.tags,
        relevanceScore = this.relevance_score,
        contentRating = this.content_rating,
        releaseYear = this.release_year,
        studio = this.studio,
        randomSeed = this.random_seed
    )
}