package io.android.data.watchlist.repository

import com.google.firebase.firestore.FirebaseFirestore
import io.android.data.watchlist.mapper.toDomain
import io.android.data.watchlist.mapper.toDto
import io.android.data.watchlist.model.WatchlistItemDto
import io.android.domain.model.WatchlistItem
import io.android.domain.repository.WatchlistRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementação concreta da fonte de dados da Watchlist utilizando Firebase Firestore.
 * Padrão BMF: 100% isolado, traduzindo DTOs para Modelos de Domínio antes de repassar para cima.
 */
class WatchlistRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : WatchlistRepository {

    override fun getUserWatchlist(userId: String): Flow<List<WatchlistItem>> = callbackFlow {
        val collectionRef = firestore
            .collection("users")
            .document(userId)
            .collection("watchlist")

        // Registra o listener em tempo real do Firestore
        val subscription = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                // Converte os documentos para DTO, filtra os nulos, mapeia para Domínio
                // e ordena para que os adicionados mais recentemente fiquem no topo da lista.
                val items = snapshot.documents
                    .mapNotNull { doc -> doc.toObject(WatchlistItemDto::class.java) }
                    .map { dto -> dto.toDomain() }
                    .sortedByDescending { it.addedAt }

                trySend(items)
            }
        }

        // Garante que o listener seja removido quando o Flow for cancelado (evita memory leaks)
        awaitClose { subscription.remove() }
    }

    override suspend fun addAnimeToWatchlist(userId: String, item: WatchlistItem): Result<Unit> {
        return try {
            val dto = item.toDto()

            firestore
                .collection("users")
                .document(userId)
                .collection("watchlist")
                .document(item.animeId) // Usar animeId como ID do documento evita itens duplicados
                .set(dto)
                .await()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun removeAnimeFromWatchlist(userId: String, animeId: String): Result<Unit> {
        return try {
            firestore
                .collection("users")
                .document(userId)
                .collection("watchlist")
                .document(animeId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}