package io.android.player.orchestrator

import io.android.domain.anime.repository.AnimeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerOrchestrator @Inject constructor(
    private val repository: AnimeRepository
) {

    /**
     * Transforma o ID bruto salvo no banco em uma URL de streaming.
     * Adicionamos o seasonId como parâmetro para a busca no Firestore funcionar.
     * BMF: Agora retorna um Pair contendo a URL do vídeo e, opcionalmente, a URL da legenda.
     */
    suspend fun getEpisodeStream(animeId: String, seasonId: String, episodeId: String): Pair<String, String?> {
        val episodes = repository.getEpisodesBySeasonId(animeId, seasonId)
        val episode = episodes.find { it.id == episodeId } ?: return Pair("", null)

        // A ÚNICA FORMA QUE FUNCIONA PARA ARQUIVOS PESADOS NO EXOPLAYER:
        val apiKey = "Obs removi a chave por segurança e por falta de tempo de criar uma estrutura mais robusta"

        // 1. Monta a URL do Vídeo
        val videoId = episode.videoUrl
        val videoStreamUrl = "https://www.googleapis.com/drive/v3/files/$videoId?alt=media&key=$apiKey"

        // 2. Monta a URL da Legenda (apenas se o banco de dados enviou um ID válido)
        val subtitleStreamUrl = episode.subtitleUrl?.takeIf { it.isNotBlank() }?.let { subId ->
            "https://www.googleapis.com/drive/v3/files/$subId?alt=media&key=$apiKey"
        }

        return Pair(videoStreamUrl, subtitleStreamUrl)
    }
}