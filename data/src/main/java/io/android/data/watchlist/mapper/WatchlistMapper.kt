package io.android.data.watchlist.mapper

import io.android.data.watchlist.model.WatchlistItemDto
import io.android.domain.model.WatchlistItem

/**
 * Extensões de mapeamento para converter os dados da camada de Data para Domain e vice-versa.
 * Garante que frameworks externos (Firebase) não contaminem as regras de negócio.
 */

/**
 * Converte um DTO vindo do Firestore em uma entidade de domínio pura.
 */
fun WatchlistItemDto.toDomain(): WatchlistItem {
    return WatchlistItem(
        animeId = this.animeId,
        title = this.title,
        coverUrl = this.coverUrl,
        addedAt = this.addedAt
    )
}

/**
 * Converte uma entidade de domínio pura em um DTO pronto para ser salvo no Firestore.
 */
fun WatchlistItem.toDto(): WatchlistItemDto {
    return WatchlistItemDto(
        animeId = this.animeId,
        title = this.title,
        coverUrl = this.coverUrl,
        addedAt = this.addedAt
    )
}