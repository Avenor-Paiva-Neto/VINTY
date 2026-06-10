package io.android.domain.usecase.watchlist

import io.android.domain.repository.WatchlistRepository
import javax.inject.Inject

/**
 * Caso de uso estritamente focado em remover uma obra da lista de interesses do usuário.
 * Padrão BMF: Implementa SRP (Single Responsibility Principle) e consome apenas a abstração.
 */
class RemoveAnimeFromWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    /**
     * @param userId O identificador único do usuário (proveniente do UserSession).
     * @param animeId O identificador único da obra a ser removida da lista.
     * @return Result encapsulando o sucesso ou a falha da operação.
     */
    suspend operator fun invoke(userId: String, animeId: String): Result<Unit> {
        return repository.removeAnimeFromWatchlist(userId, animeId)
    }
}