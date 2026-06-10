package io.android.domain.repository

import io.android.domain.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

/**
 * Interface de abstração da fonte de dados da Watchlist.
 * Segue o Princípio da Inversão de Dependência (DIP) para manter a regra de negócio isolada.
 */
interface WatchlistRepository {

    /**
     * Observa em tempo real a lista de interesses do usuário.
     * * @param userId O identificador do usuário autenticado.
     * @return Um Flow emitindo a lista de obras sempre que houver atualizações na base remota.
     */
    fun getUserWatchlist(userId: String): Flow<List<WatchlistItem>>

    /**
     * Adiciona uma nova obra à lista de interesses do usuário.
     * * @param userId O identificador do usuário autenticado.
     * @param item A entidade de domínio contendo os dados da obra.
     * @return Result encapsulando o sucesso ou a falha da operação (útil para tratamento no UseCase/ViewModel).
     */
    suspend fun addAnimeToWatchlist(userId: String, item: WatchlistItem): Result<Unit>

    /**
     * Remove uma obra específica da lista de interesses do usuário.
     * * @param userId O identificador do usuário autenticado.
     * @param animeId O identificador único da obra a ser removida.
     * @return Result encapsulando o sucesso ou a falha da operação.
     */
    suspend fun removeAnimeFromWatchlist(userId: String, animeId: String): Result<Unit>
}