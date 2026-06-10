package io.android.data.progress.mapper

import io.android.data.progress.dto.ProgressDto

/**
 * Mapper responsável pela conversão de dados entre os parâmetros de negócio
 * e o DTO que será persistido no Firestore.
 */
object ProgressMapper {

    fun toDto(
        userId: String,
        animeId: String,
        episodeId: String,
        positionMs: Long,
        durationMs: Long
    ): ProgressDto {
        return ProgressDto(
            userId = userId,
            animeId = animeId,
            episodeId = episodeId,
            positionMs = positionMs,
            durationMs = durationMs,
            lastUpdated = System.currentTimeMillis()
        )
    }
}