package io.android.domain.usecase.player

import io.android.domain.player.repository.ProgressRepository
import javax.inject.Inject

/**
 * UseCase responsável por orquestrar o salvamento do progresso.
 * Segue o princípio de responsabilidade única (SRP), delegando a persistência ao repositório.
 */
class SaveProgressUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {

    /**
     * Executa a lógica de salvar o progresso.
     * @param userId ID do usuário.
     * @param animeId ID do anime.
     * @param episodeId ID do episódio.
     * @param positionMs Posição atual do player em ms.
     * @param durationMs Duração total do vídeo em ms.
     */
    suspend operator fun invoke(
        userId: String,
        animeId: String,
        episodeId: String,
        positionMs: Long,
        durationMs: Long
    ) {
        progressRepository.saveProgress(
            userId = userId,
            animeId = animeId,
            episodeId = episodeId,
            positionMs = positionMs,
            durationMs = durationMs
        )
    }
}