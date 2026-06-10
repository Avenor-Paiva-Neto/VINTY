package io.android.discovery.orchestrator

import io.android.domain.algorithm.model.UserMetadata
import io.android.domain.algorithm.usecase.GetPersonalizedFeedUseCase
import io.android.domain.anime.model.Anime
import io.android.domain.anime.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Orchestrator: Coordena a busca de animes e a aplicação do algoritmo.
 * SRP: Sua única responsabilidade é preparar o dado final para a ViewModel.
 */
class DiscoveryOrchestrator @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val getPersonalizedFeedUseCase: GetPersonalizedFeedUseCase
) {
    /**
     * Combina a lista bruta do banco com os metadados do usuário
     * para gerar o feed final ordenado.
     */
    fun getIntegratedFeed(userMetadata: UserMetadata): Flow<List<Anime>> {
        return animeRepository.getAllAnimes().combine(
            // Aqui poderíamos combinar com outros fluxos se necessário
            kotlinx.coroutines.flow.flowOf(userMetadata)
        ) { allAnimes, metadata ->
            getPersonalizedFeedUseCase(allAnimes, metadata)
        }
    }
}