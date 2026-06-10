package io.android.domain.usecase.watchlist

import io.android.domain.model.WatchlistItem
import io.android.domain.repository.WatchlistRepository
import javax.inject.Inject

/**
 * Caso de uso estritamente focado em adicionar uma obra à lista de interesses do usuário.
 * Padrão BMF: Implementa SRP (Single Responsibility Principle) e consome apenas a abstração.
 */
class AddAnimeToWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    /**
     * @param userId O identificador único do usuário (proveniente do UserSession).
     * @param item A entidade de domínio encapsulando os dados da obra a ser salva.
     * @return Result encapsulando o sucesso ou a falha da operação.
     */
    suspend operator fun invoke(userId: String, item: WatchlistItem): Result<Unit> {
        return repository.addAnimeToWatchlist(userId, item)
    }
}