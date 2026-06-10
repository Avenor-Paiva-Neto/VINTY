package io.android.data.progress.dto

/**
 * Data Transfer Object que representa o documento de progresso no Firestore.
 * Os valores padrão são obrigatórios para que o Firebase consiga converter
 * o documento de volta para o objeto Kotlin.
 */
data class ProgressDto(
    val userId: String = "",
    val animeId: String = "",
    val episodeId: String = "",
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val lastUpdated: Long = 0L
)