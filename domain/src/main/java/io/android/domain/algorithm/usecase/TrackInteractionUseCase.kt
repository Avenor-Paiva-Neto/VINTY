package io.android.domain.algorithm.usecase

import io.android.domain.algorithm.model.AlgorithmConfig
import io.android.domain.algorithm.model.UserMetadata
import javax.inject.Inject

class TrackInteractionUseCase @Inject constructor() {

    fun execute(
        currentMetadata: UserMetadata,
        tags: List<String>,
        interactionWeight: Int
    ): UserMetadata {
        val newScores = currentMetadata.tagScores.toMutableMap()

        tags.forEach { tag ->
            val currentScore = newScores[tag] ?: 0
            newScores[tag] = currentScore + interactionWeight
        }

        return currentMetadata.copy(
            tagScores = newScores,
            lastSyncTimestamp = System.currentTimeMillis()
        )
    }
}