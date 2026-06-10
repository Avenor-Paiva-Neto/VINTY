package io.craemza.watchlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.android.domain.model.auth.AuthState
import io.android.domain.repository.AuthRepository
import io.android.domain.usecase.watchlist.GetUserWatchlistUseCase
import io.android.domain.usecase.watchlist.RemoveAnimeFromWatchlistUseCase
import io.craemza.watchlist.presentation.state.WatchlistUiEvent
import io.craemza.watchlist.presentation.state.WatchlistUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsável por gerenciar o estado e os eventos da tela de Watchlist.
 * Reativo à sessão do usuário: só consome os dados do banco se houver um usuário ativo.
 */
@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUserWatchlistUseCase: GetUserWatchlistUseCase,
    private val removeAnimeFromWatchlistUseCase: RemoveAnimeFromWatchlistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WatchlistUiState>(WatchlistUiState.Loading)
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null
    private var watchlistJob: Job? = null

    init {
        observeAuthenticationState()
    }

    /**
     * Processa os eventos/intenções disparados pela UI.
     */
    fun onEvent(event: WatchlistUiEvent) {
        when (event) {
            is WatchlistUiEvent.OnRemoveClick -> removeAnime(event.animeId)
            is WatchlistUiEvent.OnAnimeClick -> {
                // A navegação para o Player será tratada diretamente nos callbacks da UI (Screen).
                // Caso precise de lógicas extras de analytics no futuro, podem ser inseridas aqui.
            }
        }
    }

    /**
     * Observa o estado global de autenticação.
     * Aciona o carregamento da lista apenas quando o usuário está Autenticado.
     */
    private fun observeAuthenticationState() {
        viewModelScope.launch {
            authRepository.authState.collectLatest { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        currentUserId = authState.session.userId
                        observeWatchlist(authState.session.userId)
                    }
                    is AuthState.Unauthenticated -> {
                        currentUserId = null
                        watchlistJob?.cancel() // Para de escutar o Firestore se deslogar
                        _uiState.value = WatchlistUiState.Unauthenticated
                    }
                    is AuthState.Loading, AuthState.Idle -> {
                        _uiState.value = WatchlistUiState.Loading
                    }
                    is AuthState.Error -> {
                        _uiState.value = WatchlistUiState.Error(authState.message)
                    }
                }
            }
        }
    }

    /**
     * Observa a lista de animes do Firestore em tempo real.
     */
    private fun observeWatchlist(userId: String) {
        // Cancela o job anterior caso exista para evitar múltiplos listeners abertos
        watchlistJob?.cancel()

        watchlistJob = viewModelScope.launch {
            getUserWatchlistUseCase(userId)
                .catch { exception ->
                    _uiState.value = WatchlistUiState.Error(exception.message ?: "Erro desconhecido ao carregar lista.")
                }
                .collect { items ->
                    _uiState.value = WatchlistUiState.Success(items)
                }
        }
    }

    /**
     * Remove o anime da lista usando o caso de uso apropriado.
     */
    private fun removeAnime(animeId: String) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            val result = removeAnimeFromWatchlistUseCase(userId, animeId)
            if (result.isFailure) {
                // Como temos um fluxo reativo em tempo real, se a remoção falhar
                // podemos notificar o usuário alterando o estado para Error temporário ou via Snackbar (Effect).
                // Para manter a UI íntegra, logamos ou atualizamos o estado.
                _uiState.value = WatchlistUiState.Error("Falha ao remover o anime da lista.")
            }
        }
    }
}