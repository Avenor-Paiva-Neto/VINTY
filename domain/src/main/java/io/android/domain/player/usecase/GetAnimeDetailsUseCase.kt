package io.android.domain.player.usecase

import io.android.domain.anime.model.Anime
import io.android.domain.anime.model.Episode
import io.android.domain.anime.model.Season
import io.android.domain.anime.repository.AnimeRepository
import javax.inject.Inject

/**
 * Agrega a estrutura de "Boneca Russa" do banco em um formato pronto para a UI do Player.
 */
data class SeasonWithEpisodes(
    val season: Season,
    val episodes: List<Episode> = emptyList() // Vazio até que a temporada seja expandida
)

/**
 * Representa o estado completo dos dados necessários para a tela de Detalhes/Player.
 */
data class AnimeDetails(
    val anime: Anime,
    val seasons: List<SeasonWithEpisodes>
)

/**
 * Orquestra a busca dos metadados da obra, suas temporadas e pré-carrega
 * os episódios da primeira temporada para exibição imediata.
 */
class GetAnimeDetailsUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    /**
     * @param animeId O ID da obra clicada no Discovery.
     * @return AnimeDetails completo ou null se a obra não for encontrada.
     */
    suspend operator fun invoke(animeId: String): AnimeDetails? {
        // 1. Busca os metadados principais (Hero, Title, Description, etc.)
        val anime = repository.getAnimeById(animeId) ?: return null

        // 2. Busca todas as temporadas disponíveis para a obra
        val seasons = repository.getSeasonsByAnimeId(animeId)

        // 3. Monta a lista agregada com a lógica de "Lazy Loading" (Carregamento sob demanda)
        val seasonsWithEpisodes = seasons.mapIndexed { index, season ->
            if (index == 0) {
                // Para a primeira temporada (índice 0), já buscamos os episódios automaticamente.
                // Isso garante que a lista de "Mine Cards" já apareça montada na tela.
                val episodes = repository.getEpisodesBySeasonId(animeId, season.id)
                    .sortedBy { it.number } // <-- A ÚNICA MUDANÇA ESTÁ AQUI: ordenando por 'number'

                SeasonWithEpisodes(season, episodes)
            } else {
                // Para as demais temporadas, devolvemos a lista de episódios vazia.
                // Eles só serão buscados pelo ViewModel quando o usuário clicar na "setinha" do Accordion.
                SeasonWithEpisodes(season, emptyList())
            }
        }

        // 4. Retorna o pacote completo e imutável para a apresentação
        return AnimeDetails(
            anime = anime,
            seasons = seasonsWithEpisodes
        )
    }
}