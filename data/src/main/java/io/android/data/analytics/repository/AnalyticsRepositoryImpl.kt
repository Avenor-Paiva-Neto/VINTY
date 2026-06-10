package io.android.data.analytics.repository

import io.android.data.analytics.database.AnalyticsDao
import io.android.data.analytics.model.AnalyticsEventEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val analyticsDao: AnalyticsDao,
    private val firestore: FirebaseFirestore
) {
    /**
     * Salva o evento no buffer local (Room)
     */
    suspend fun trackEventLocal(animeId: String, tags: List<String>, weight: Int) {
        val entity = AnalyticsEventEntity(
            animeId = animeId,
            tags = tags.joinToString(","),
            weight = weight
        )
        analyticsDao.insertEvent(entity)
    }

    /**
     * O "Flush": Pega tudo o que está pendente no Room e sincroniza com o Firestore
     * em uma única operação atômica (Transaction).
     */
    suspend fun syncWithFirestore(userId: String) {
        val pendingEvents = analyticsDao.getUnsyncedEvents()
        if (pendingEvents.isEmpty()) return

        val userMetadataRef = firestore.collection("users").document(userId)
            .collection("metadata").document("algorithm")

        firestore.runTransaction { transaction ->
            pendingEvents.forEach { event ->
                val tags = event.tags.split(",")
                tags.forEach { tag ->
                    // Incrementa o score da tag diretamente no Firestore
                    transaction.update(userMetadataRef, "tag_scores.$tag", FieldValue.increment(event.weight.toLong()))
                }
            }
        }.addOnSuccessListener {
            // Após sucesso na nuvem, limpamos o local
            // analyticsDao.markAsSynced(pendingEvents)
        }
    }
}