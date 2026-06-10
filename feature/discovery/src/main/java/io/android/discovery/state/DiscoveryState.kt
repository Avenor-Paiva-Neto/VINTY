package io.android.discovery.state

import io.android.domain.anime.model.Anime

/**
 * Representa o estado exato da UI.
 * Cada campo aqui será desenhado por um componente do Jetpack Compose.
 */
sealed class DiscoveryState {
    data object Loading : DiscoveryState()

    data class Success(
        val heroAnime: Anime?,          // O Anime com maior Score (Capa)
        val continueWatching: List<AnimeWithProgress>, // Lista com progresso
        val recommended: List<Anime>,   // "Para Você" (Algoritmo)
        val handPicked: List<Anime>     // "Escolhidos a dedo" (Curadoria/Aleatório)
    ) : DiscoveryState()

    data class Error(val message: String) : DiscoveryState()
}

// Wrapper para unir o Anime ao progresso do usuário
data class AnimeWithProgress(
    val anime: Anime,
    val progress: Float // 0.0 a 1.0
)