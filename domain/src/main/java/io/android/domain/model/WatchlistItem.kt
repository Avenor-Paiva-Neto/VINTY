package io.android.domain.model

/**
 * Representação pura de um item na lista de interesses do usuário no ecossistema VINTY.
 * Entidade de domínio estritamente mapeada, sem anotações de frameworks externos (ex: @DocumentId do Firebase).
 *
 * @param animeId Identificador único da obra, usado para navegação posterior para o Player.
 * @param title Título da obra.
 * @param coverUrl URL da imagem de capa.
 * @param addedAt Timestamp de quando o usuário adicionou o item à lista (útil para ordenação).
 */
data class WatchlistItem(
    val animeId: String,
    val title: String,
    val coverUrl: String,
    val addedAt: Long
)