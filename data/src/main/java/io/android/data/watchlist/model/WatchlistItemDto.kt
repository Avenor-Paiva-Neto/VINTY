package io.android.data.watchlist.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

/**
 * DTO (Data Transfer Object) representando um item da watchlist no Firestore.
 * Caminho no banco: users/{userId}/watchlist/{animeId}
 * * Necessita de valores padrão para que o Kotlin gere um construtor vazio,
 * exigência do SDK do Firebase para desserialização automática.
 */
data class WatchlistItemDto(
    @DocumentId
    val animeId: String = "",

    @get:PropertyName("title")
    @set:PropertyName("title")
    var title: String = "",

    @get:PropertyName("coverUrl")
    @set:PropertyName("coverUrl")
    var coverUrl: String = "",

    @get:PropertyName("addedAt")
    @set:PropertyName("addedAt")
    var addedAt: Long = 0L
)