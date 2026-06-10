package io.android.domain.player.repository

import kotlinx.coroutines.flow.Flow

/**
 * Interface que define o contrato para persistência e recuperação do progresso de um vídeo.
 * Esta interface reside na camada de Domínio, garantindo o desacoplamento de implementações
 * externas (Room, Firestore, etc.).
 */
interface ProgressRepository {

    /**
     * Salva o progresso atual de um episódio específico.
     * @param userId ID do usuário logado.
     * @param animeId ID do anime.
     * @param episodeId ID do episódio.
     * @param positionMs Posição atual em milissegundos.
     * @param durationMs Duração total do vídeo em milissegundos.
     */
    suspend fun saveProgress(
        userId: String,
        animeId: String,
        episodeId: String,
        positionMs: Long,
        durationMs: Long
    )

    /**
     * Recupera o progresso salvo de um episódio específico.
     * @return A posição em milissegundos ou null se não houver progresso.
     */
    suspend fun getProgress(
        userId: String,
        animeId: String,
        episodeId: String
    ): Long?

    /**
     * Opcional: Flow para observar mudanças no progresso (útil para atualizar UI em tempo real).
     */
    fun observeProgress(userId: String, animeId: String): Flow<Map<String, Long>>
}