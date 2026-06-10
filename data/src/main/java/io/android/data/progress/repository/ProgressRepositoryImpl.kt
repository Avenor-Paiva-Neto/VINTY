package io.android.data.progress.repository

import com.google.firebase.firestore.FirebaseFirestore
import io.android.data.progress.dto.ProgressDto
import io.android.data.progress.mapper.ProgressMapper
import io.android.domain.player.repository.ProgressRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProgressRepository {

    private val collection = firestore.collection("user_progress")

    override suspend fun saveProgress(
        userId: String,
        animeId: String,
        episodeId: String,
        positionMs: Long,
        durationMs: Long
    ) {
        val dto = ProgressMapper.toDto(userId, animeId, episodeId, positionMs, durationMs)
        val documentId = "${userId}_${episodeId}"

        // set com merge=true garante que não sobrescrevemos outros campos se houver
        collection.document(documentId).set(dto).await()
    }

    override suspend fun getProgress(
        userId: String,
        animeId: String,
        episodeId: String
    ): Long? {
        val documentId = "${userId}_${episodeId}"
        val snapshot = collection.document(documentId).get().await()

        return snapshot.toObject(ProgressDto::class.java)?.positionMs
    }

    override fun observeProgress(userId: String, animeId: String): Flow<Map<String, Long>> = callbackFlow {
        // Query para observar todos os episódios de um usuário específico para um anime
        val query = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("animeId", animeId)

        val listener = query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }

            val progressMap = snapshot?.documents?.associate { doc ->
                val dto = doc.toObject(ProgressDto::class.java)
                (dto?.episodeId ?: "") to (dto?.positionMs ?: 0L)
            } ?: emptyMap()

            trySend(progressMap)
        }

        awaitClose { listener.remove() }
    }
}