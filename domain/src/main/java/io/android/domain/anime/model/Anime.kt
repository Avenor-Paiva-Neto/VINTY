package io.android.domain.anime.model

/**
 * Representação pura de uma obra (Anime).
 * Sem frameworks, sem anotações, apenas lógica de negócio.
 */
data class Anime(
    val id: String,
    val title: String,
    val description: String,
    val coverUrl: String,
    val bannerUrl: String,
    val tags: List<String>,
    val relevanceScore: Int = 0,
    val contentRating: String,
    val releaseYear: Int,
    val studio: String,
    val randomSeed: Int,
    val isFavorite: Boolean = false
) {
    /**
     * Lógica de domínio: Calcula a força da recomendação baseada em pesos.
     */
    fun calculateMatchScore(userTagScores: Map<String, Int>): Int {
        val tagSum = tags.sumOf { tag -> userTagScores[tag] ?: 0 }
        return tagSum + relevanceScore
    }
}