package io.android.domain.usecase.watchlist

import io.android.domain.model.WatchlistItem
import io.android.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso estritamente focado em recuperar a lista de interesses do usuário em tempo real.
 * Padrão BMF: Implementa SRP (Single Responsibility Principle) e consome apenas a abstração.
 */
class GetUserWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    /**
     * @param userId O identificador único do usuário (extraído reativamente do UserSession).
     * @return Flow reativo contendo a lista atualizada para alimentar o fluxo unidirecional (UDF).
     */
    operator fun invoke(userId: String): Flow<List<WatchlistItem>> {
        return repository.getUserWatchlist(userId)
    }
}