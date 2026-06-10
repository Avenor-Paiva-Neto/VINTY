package io.android.data.anime.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import io.android.data.anime.mapper.toDomain
import io.android.data.anime.model.AnimeDto
import io.android.data.anime.model.EpisodeDto
import io.android.data.anime.model.SeasonDto
import io.android.domain.anime.model.Anime
import io.android.domain.anime.model.Episode
import io.android.domain.anime.model.Season
import io.android.domain.anime.repository.AnimeRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AnimeRepository {

    private val TAG = "VINTY_DEBUG"

    override fun getAllAnimes(): Flow<List<Anime>> = callbackFlow {
        Log.d(TAG, "🔥 Repo: Iniciando escuta da coleção 'animes'...")

        val subscription = firestore.collection("animes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "🔴 Repo: Erro no Firestore: ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot == null || snapshot.isEmpty) {
                    Log.w(TAG, "⚠️ Repo: Snapshot vazio.")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                try {
                    val animes = snapshot.documents.mapNotNull { doc ->
                        val dto = doc.toObject(AnimeDto::class.java)
                        dto?.copy(id = doc.id)?.toDomain()
                    }
                    trySend(animes)
                } catch (e: Exception) {
                    Log.e(TAG, "💥 Repo: Erro no mapeamento: ${e.message}")
                }
            }

        awaitClose { subscription.remove() }
    }

    override suspend fun getAnimeById(id: String): Anime? {
        return try {
            val document = firestore.collection("animes").document(id).get().await()
            document.toObject(AnimeDto::class.java)?.copy(id = document.id)?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "🔴 Repo: Erro ao buscar Anime $id: ${e.message}")
            null
        }
    }

    override suspend fun toggleFavorite(animeId: String, isFavorite: Boolean) {
        // Implementação futura conforme regras BMF
    }

    override fun getAnimesByTag(tag: String): Flow<List<Anime>> = callbackFlow {
        val query = firestore.collection("animes")
            .whereArrayContains("tags", tag)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val animes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(AnimeDto::class.java)?.copy(id = doc.id)?.toDomain()
                } ?: emptyList()
                trySend(animes)
            }
        awaitClose { query.remove() }
    }

    /**
     * Busca subcoleção 'seasons' dentro do documento do anime.
     */
    override suspend fun getSeasonsByAnimeId(animeId: String): List<Season> {
        return try {
            Log.d(TAG, "📦 Repo: Buscando temporadas para o anime: $animeId")
            val snapshot = firestore.collection("animes")
                .document(animeId)
                .collection("seasons")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(SeasonDto::class.java)
                    ?.copy(id = doc.id)
                    ?.toDomain()
            }
        } catch (e: Exception) {
            Log.e(TAG, "🔴 Repo: Erro ao buscar temporadas: ${e.message}")
            emptyList()
        }
    }

    /**
     * Busca subcoleção 'episodes' dentro do documento da temporada.
     * Caminho: animes/{animeId}/seasons/{seasonId}/episodes
     */
    override suspend fun getEpisodesBySeasonId(animeId: String, seasonId: String): List<Episode> {
        return try {
            Log.d(TAG, "📦 Repo: Buscando episódios -> Anime: $animeId | Season: $seasonId")
            val snapshot = firestore.collection("animes")
                .document(animeId)
                .collection("seasons")
                .document(seasonId)
                .collection("episodes")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(EpisodeDto::class.java)
                    ?.copy(id = doc.id)
                    ?.toDomain()
            }
        } catch (e: Exception) {
            Log.e(TAG, "🔴 Repo: Erro ao buscar episódios: ${e.message}")
            emptyList()
        }
    }
}