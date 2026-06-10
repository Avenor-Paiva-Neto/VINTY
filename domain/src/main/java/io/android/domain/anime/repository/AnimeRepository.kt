package io.android.domain.anime.repository

import io.android.domain.anime.model.Anime
import io.android.domain.anime.model.Episode
import io.android.domain.anime.model.Season
import kotlinx.coroutines.flow.Flow

/**
 * Interface de abstração seguindo o Princípio da Inversão de Dependência (DIP).
 * Esta é a 'Porta' de entrada para qualquer dado relacionado ao catálogo.
 */
interface AnimeRepository {

    /**
     * Observa a lista global de animes para o Discovery.
     */
    fun getAllAnimes(): Flow<List<Anime>>

    /**
     * Busca os metadados principais de uma obra específica.
     */
    suspend fun getAnimeById(id: String): Anime?

    /**
     * Altera o estado de favorito de uma obra.
     */
    suspend fun toggleFavorite(animeId: String, isFavorite: Boolean)

    /**
     * Filtra obras por categoria/tag.
     */
    fun getAnimesByTag(tag: String): Flow<List<Anime>>

    /**
     * Busca a lista de temporadas de um anime específico.
     * Implementação voltada para a estrutura de subcoleções do Firestore.
     */
    suspend fun getSeasonsByAnimeId(animeId: String): List<Season>

    /**
     * Busca a lista de episódios de uma temporada específica.
     * @param animeId Necessário para compor o caminho da subcoleção.
     * @param seasonId ID da temporada pai.
     */
    suspend fun getEpisodesBySeasonId(animeId: String, seasonId: String): List<Episode>
}