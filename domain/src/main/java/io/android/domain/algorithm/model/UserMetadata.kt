package io.android.domain.algorithm.model

/**
 * Representação do perfil comportamental do usuário.
 */
data class UserMetadata(
    val userId: String,
    val tagScores: Map<String, Int>, // Ex: {"Cyberpunk": 150, "90s": 40}
    val watchHistory: Map<String, Double>, // Ex: {"animeId_epId": 0.85 (85% visto)}
    val favoriteIds: List<String>,
    val lastSyncTimestamp: Long
)