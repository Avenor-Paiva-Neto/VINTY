package io.android.domain.algorithm.usecase

import io.android.domain.algorithm.model.AlgorithmConfig
import io.android.domain.algorithm.model.UserMetadata
import io.android.domain.anime.model.Anime
import javax.inject.Inject

/**
 * UseCase responsável por transformar a lista bruta em um feed inteligente.
 */
class GetPersonalizedFeedUseCase @Inject constructor() {

    operator fun invoke(
        allAnimes: List<Anime>,
        userMetadata: UserMetadata
    ): List<Anime> {
        if (allAnimes.isEmpty()) return emptyList()

        // 1. Calcula o Match Score para cada anime
        val scoredAnimes = allAnimes.map { anime ->
            anime to anime.calculateMatchScore(userMetadata.tagScores)
        }

        // 2. Separa por relevância (Ordenação Decrescente)
        val sortedAnimes = scoredAnimes
            .sortedByDescending { it.second }
            .map { it.first }

        // 3. Aplica Serendipidade (Troca alguns itens do topo por aleatórios para evitar bolha)
        return applySerendipity(sortedAnimes)
    }

    private fun applySerendipity(orderedList: List<Anime>): List<Anime> {
        val totalToShuffle = (orderedList.size * AlgorithmConfig.SERENDIPITY_FACTOR).toInt()
        if (totalToShuffle <= 1) return orderedList

        val result = orderedList.toMutableList()
        val tail = result.takeLast(orderedList.size / 2).shuffled()

        // Insere elementos do "fim" da lista em posições aleatórias do "topo"
        for (i in 0 until totalToShuffle) {
            val randomIndex = (0 until orderedList.size / 2).random()
            if (i < tail.size) {
                result[randomIndex] = tail[i]
            }
        }
        return result
    }
}