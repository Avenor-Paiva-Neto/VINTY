package io.craemza.watchlist.presentation.state

import io.android.domain.model.WatchlistItem

/**
 * Representa os estados imutáveis possíveis da tela de Watchlist.
 * Segue o padrão de Fluxo Unidirecional de Dados (UDF).
 */
sealed interface WatchlistUiState {

    /**
     * Estado inicial ou durante o carregamento de dados.
     */
    data object Loading : WatchlistUiState

    /**
     * Estado de sucesso, contendo a lista de interesses pronta para exibição.
     * (A verificação de lista vazia será feita na UI lendo items.isEmpty()).
     */
    data class Success(val items: List<WatchlistItem>) : WatchlistUiState

    /**
     * Estado acionado caso a sessão do usuário seja revogada ou ele não esteja logado.
     */
    data object Unauthenticated : WatchlistUiState

    /**
     * Estado de falha ao tentar recuperar a lista do Firestore.
     */
    data class Error(val message: String) : WatchlistUiState
}