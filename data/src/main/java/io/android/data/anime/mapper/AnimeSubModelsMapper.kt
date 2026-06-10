package io.android.data.anime.mapper

import io.android.data.anime.model.EpisodeDto
import io.android.data.anime.model.SeasonDto
import io.android.domain.anime.model.Episode
import io.android.domain.anime.model.Season

/**
 * Mapeia o DTO de Temporada para o modelo de Domínio.
 */
fun SeasonDto.toDomain(): Season {
    return Season(
        id = this.id,
        title = this.title,
        audioType = this.audio_type,
        driveFolderId = this.drive_folder_id,
        defaultThumbUrl = this.default_episode_thumb,
        episodeCount = this.episode_count
    )
}

/**
 * Mapeia o DTO de Episódio para o modelo de Domínio.
 * BMF: Atualizado para refletir a nova estrutura limpa, ligando os pontos corretos.
 */
fun EpisodeDto.toDomain(): Episode {
    return Episode(
        id = this.id, // O ID do documento no Firestore (ex: "ep_1")
        title = this.title,
        number = this.number.toLong(), // Convertendo para Long, que é o padrão recomendado para números no Firestore/Domain
        videoUrl = this.video_url, // Agora pega diretamente a string do ID do Drive salvo no banco
        duration = this.duration,
        thumbUrl = this.thumb_url,
        subtitleUrl = this.subtitle_url // Repassa o ID da legenda, que pode ser null
    )
}