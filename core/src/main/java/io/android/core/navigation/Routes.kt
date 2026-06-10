package io.android.core.navigation

import kotlinx.serialization.Serializable


/**
 * Definição centralizada e Type-Safe das rotas.
 */
sealed interface Screen {

    // Use 'data object' para rotas sem argumentos (Melhor prática Kotlin)
    @Serializable
    data object Auth : Screen

    @Serializable
    data object Discovery : Screen

    @Serializable
    data object Watchlist : Screen

    // Rota com argumentos
    @Serializable
    data class Player(val animeId: String, val episodeId: String = "ep_1") : Screen
    @Serializable data object Profile : Screen
}