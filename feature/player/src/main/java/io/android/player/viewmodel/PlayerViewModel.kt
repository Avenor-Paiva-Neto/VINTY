package io.android.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.android.domain.anime.repository.AnimeRepository
import io.android.domain.model.WatchlistItem
import io.android.domain.model.auth.AuthState
import io.android.domain.player.model.PlaybackState
import io.android.domain.player.usecase.AnimeDetails
import io.android.domain.player.usecase.GetAnimeDetailsUseCase
import io.android.domain.repository.AuthRepository
import io.android.domain.usecase.player.GetProgressUseCase
import io.android.domain.usecase.player.SaveProgressUseCase
import io.android.domain.usecase.watchlist.AddAnimeToWatchlistUseCase
import io.android.domain.usecase.watchlist.GetUserWatchlistUseCase
import io.android.domain.usecase.watchlist.RemoveAnimeFromWatchlistUseCase
import io.android.player.logic.PlaybackTracker
import io.android.player.orchestrator.PlayerOrchestrator
import io.android.player.provider.PlayerProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estados da interface de detalhes/player.
 */
sealed class PlayerUiState {
    data object Loading : PlayerUiState()
    data class Success(val details: AnimeDetails) : PlayerUiState()
    data class Error(val message: String) : PlayerUiState()
}

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val orchestrator: PlayerOrchestrator,
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val repository: AnimeRepository,
    val playerProvider: PlayerProvider,
    private val authRepository: AuthRepository,
    private val getUserWatchlistUseCase: GetUserWatchlistUseCase,
    private val addAnimeToWatchlistUseCase: AddAnimeToWatchlistUseCase,
    private val removeAnimeFromWatchlistUseCase: RemoveAnimeFromWatchlistUseCase,
    // BMF: Injeção dos novos componentes de progresso
    private val saveProgressUseCase: SaveProgressUseCase,
    private val getProgressUseCase: GetProgressUseCase,
    private val playbackTracker: PlaybackTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    // BMF: Novo estado para observar se a obra atual está na Watchlist do usuário
    private val _isWatchlisted = MutableStateFlow(false)
    val isWatchlisted: StateFlow<Boolean> = _isWatchlisted.asStateFlow()

    private var currentVideoUrl: String? = null
    private var currentUserId: String? = null
    private var watchlistJob: Job? = null

    // BMF: Job para gerenciar o ciclo de vida do rastreador de progresso
    private var progressTrackingJob: Job? = null

    init {
        observeAuthenticationState()
    }

    /**
     * Observa a sessão do usuário. Necessário para saber de qual banco buscar a Watchlist.
     */
    private fun observeAuthenticationState() {
        viewModelScope.launch {
            authRepository.authState.collectLatest { authState ->
                if (authState is AuthState.Authenticated) {
                    currentUserId = authState.session.userId
                    // Se o anime já foi carregado, inicia a observação da watchlist imediatamente
                    val state = _uiState.value
                    if (state is PlayerUiState.Success) {
                        observeWatchlist(state.details.anime.id)
                    }
                } else {
                    currentUserId = null
                    watchlistJob?.cancel()
                    _isWatchlisted.value = false
                }
            }
        }
    }

    /**
     * Carrega os dados iniciais (Anime + Temporada 1) e aciona a verificação da Watchlist.
     */
    fun loadAnimeDetails(animeId: String) {
        viewModelScope.launch {
            _uiState.value = PlayerUiState.Loading
            val details = getAnimeDetailsUseCase(animeId)
            if (details != null) {
                _uiState.value = PlayerUiState.Success(details)
                observeWatchlist(animeId) // Engata a escuta no Firestore para essa obra
            } else {
                _uiState.value = PlayerUiState.Error("Falha ao carregar detalhes da obra.")
            }
        }
    }

    /**
     * Mantém um listener ativo no Firestore. Se o usuário adicionar/remover o anime
     * em outro dispositivo, a UI atualizará o botão em tempo real.
     */
    private fun observeWatchlist(animeId: String) {
        val userId = currentUserId ?: return
        watchlistJob?.cancel()
        watchlistJob = viewModelScope.launch {
            getUserWatchlistUseCase(userId).collectLatest { items ->
                _isWatchlisted.value = items.any { it.animeId == animeId }
            }
        }
    }

    /**
     * Alterna o status da obra na Watchlist (Adiciona se não tem, remove se tem).
     */
    fun toggleWatchlist() {
        val userId = currentUserId ?: return
        val state = _uiState.value

        if (state !is PlayerUiState.Success) return

        val anime = state.details.anime
        val isCurrentlyWatchlisted = _isWatchlisted.value

        viewModelScope.launch {
            if (isCurrentlyWatchlisted) {
                removeAnimeFromWatchlistUseCase(userId, anime.id)
            } else {
                val item = WatchlistItem(
                    animeId = anime.id,
                    title = anime.title,
                    coverUrl = anime.coverUrl,
                    addedAt = System.currentTimeMillis()
                )
                addAnimeToWatchlistUseCase(userId, item)
            }
        }
    }

    /**
     * Lazy Loading de episódios ao expandir temporadas adicionais.
     */
    fun onExpandSeason(animeId: String, seasonId: String) {
        val currentState = _uiState.value
        if (currentState !is PlayerUiState.Success) return

        val targetSeason = currentState.details.seasons.find { it.season.id == seasonId }
        if (targetSeason == null || targetSeason.episodes.isNotEmpty()) return

        viewModelScope.launch {
            val newEpisodes = repository.getEpisodesBySeasonId(animeId, seasonId).sortedBy { it.number }
            _uiState.update { state ->
                if (state is PlayerUiState.Success) {
                    val updated = state.details.seasons.map {
                        if (it.season.id == seasonId) it.copy(episodes = newEpisodes) else it
                    }
                    PlayerUiState.Success(state.details.copy(seasons = updated))
                } else state
            }
        }
    }

    /**
     * Inicia a reprodução do episódio selecionado.
     */
    fun onPlayEpisode(animeId: String, seasonId: String, episodeId: String) {
        viewModelScope.launch {
            _playbackState.value = PlaybackState.Buffering

            // BMF: Busca URL do vídeo e o progresso salvo em paralelo (ou sequencial rápido)
            val (videoUrl, subtitleUrl) = orchestrator.getEpisodeStream(animeId, seasonId, episodeId)
            val savedPosition = currentUserId?.let { getProgressUseCase(it, animeId, episodeId) } ?: 0L

            if (videoUrl.isNotEmpty()) {
                currentVideoUrl = videoUrl
                playerProvider.prepareAndPlay(videoUrl, subtitleUrl)

                // BMF: Se houver progresso, aplica o seekTo antes de exibir
                if (savedPosition > 0L) {
                    playerProvider.getPlayer().seekTo(savedPosition)
                }

                _playbackState.value = PlaybackState.Playing(
                    currentPosition = savedPosition,
                    duration = 0L
                )

                // BMF: Inicia o rastreamento real baseado no player injetado
                startTrackingProgress(animeId, episodeId)

            } else {
                _playbackState.value = PlaybackState.Error("Link de transmissão não disponível.")
            }
        }
    }

    /**
     * BMF: Lógica de rastreamento reativo usando o PlaybackTracker.
     */
    private fun startTrackingProgress(animeId: String, episodeId: String) {
        progressTrackingJob?.cancel()
        progressTrackingJob = viewModelScope.launch {
            val player = playerProvider.getPlayer()

            playbackTracker.track(
                getCurrentPosition = { player.currentPosition },
                getDuration = { player.duration }
            ).collectLatest { percent ->
                currentUserId?.let { userId ->
                    val duration = player.duration
                    val currentPos = (percent * duration).toLong()
                    saveProgressUseCase(userId, animeId, episodeId, currentPos, duration)
                }
            }
        }
    }

    fun getCurrentUrl(): String? = currentVideoUrl

    override fun onCleared() {
        super.onCleared()
        playerProvider.releasePlayer()
        watchlistJob?.cancel()
        // BMF: Limpeza obrigatória do job de tracking
        progressTrackingJob?.cancel()
    }
}