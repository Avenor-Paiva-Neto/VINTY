package io.craemza.watchlist.presentation.state

/**
 * Representa as intenções (ações) disparadas pelo usuário na tela de Watchlist.
 * Segue o padrão de Fluxo Unidirecional de Dados (UDF) (Intents do MVI/MVVM).
 */
sealed interface WatchlistUiEvent {

    /**
     * Disparado quando o usuário clica no card da obra para abrir o Player.
     * @param animeId O identificador único da obra para a navegação.
     */
    data class OnAnimeClick(val animeId: String) : WatchlistUiEvent

    /**
     * Disparado quando o usuário clica no ícone/botão para remover a obra da lista.
     * @param animeId O identificador único da obra a ser removida.
     */
    data class OnRemoveClick(val animeId: String) : WatchlistUiEvent
}