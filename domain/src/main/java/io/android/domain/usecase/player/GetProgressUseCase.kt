package io.android.domain.usecase.player

import io.android.domain.player.repository.ProgressRepository
import javax.inject.Inject

/**
 * UseCase responsável por buscar o progresso salvo de um episódio.
 * Segue o princípio de responsabilidade única (SRP), delegando a consulta ao repositório.
 */
class GetProgressUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {

    /**
     * Executa a lógica de buscar o progresso.
     * @param userId ID do usuário.
     * @param animeId ID do anime.
     * @param episodeId ID do episódio.
     * @return A posição em milissegundos, ou null se não houver progresso salvo.
     */
    suspend operator fun invoke(
        userId: String,
        animeId: String,
        episodeId: String
    ): Long? {
        return progressRepository.getProgress(userId, animeId, episodeId)
    }
}