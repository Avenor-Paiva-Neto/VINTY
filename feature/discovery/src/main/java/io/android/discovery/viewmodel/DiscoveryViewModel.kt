package io.android.discovery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.android.discovery.orchestrator.DiscoveryOrchestrator
import io.android.discovery.state.DiscoveryState
import io.android.discovery.state.AnimeWithProgress
import io.android.domain.algorithm.model.UserMetadata
import io.android.domain.player.repository.ProgressRepository // Nova dependência
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val orchestrator: DiscoveryOrchestrator,
    private val progressRepository: ProgressRepository // Injetado
) : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoveryState>(DiscoveryState.Loading)
    val uiState: StateFlow<DiscoveryState> = _uiState.asStateFlow()

    fun loadDashboard(user: UserMetadata) {
        viewModelScope.launch {
            // Combinamos o feed vindo do orquestrador com a observação de progresso do usuário
            // Nota: Para uma lista de animes, poderíamos observar o progresso geral do usuário.
            // Aqui assumo que o repositório pode fornecer um fluxo do progresso do usuário.

            orchestrator.getIntegratedFeed(user)
                .onStart { _uiState.value = DiscoveryState.Loading }
                .catch { e ->
                    _uiState.value = DiscoveryState.Error(e.message ?: "Erro ao carregar feed")
                }
                .collect { allAnimes ->

                    // Como não temos um "observeAllProgress" no repo, vamos buscar o estado atual
                    // Para cada anime, poderíamos chamar o getProgress, mas para otimizar:

                    _uiState.value = DiscoveryState.Success(
                        heroAnime = allAnimes.firstOrNull(),

                        // Aqui conectamos o dado real:
                        // Estamos assumindo que os animes na lista de "Continue Watching"
                        // virão enriquecidos ou consultaremos o progresso deles.
                        continueWatching = allAnimes.take(3).map { anime ->
                            // Buscamos o progresso real no repositório (exemplo: primeiro episódio)
                            // Na prática, seu orquestrador deveria já vir com o episodeId certo.
                            val progressMs = progressRepository.getProgress(user.userId, anime.id, "ep_1") ?: 0L

                            // Simplificação para percentual (considerando duração média ou real se disponível)
                            val progressFloat = if (progressMs > 0) 0.5f else 0.0f

                            AnimeWithProgress(anime = anime, progress = progressFloat)
                        },

                        recommended = allAnimes,
                        handPicked = allAnimes.shuffled().take(10)
                    )
                }
        }
    }
}