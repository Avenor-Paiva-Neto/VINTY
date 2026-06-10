package io.android.data.anime.model

import com.google.firebase.firestore.PropertyName

/**
 * DTO para a subcoleção "seasons".
 */
data class SeasonDto(
    val id: String = "",
    val title: String = "",

    @PropertyName("audio_type")
    val audio_type: String = "",

    @PropertyName("drive_folder_id")
    val drive_folder_id: String = "",

    @PropertyName("default_episode_thumb")
    val default_episode_thumb: String = "",

    @PropertyName("episode_count")
    val episode_count: Int = 0,

    val number: Int = 0,
    val description: String = ""
)

/**
 * DTO para a subcoleção "episodes".
 * BMF: Atualizado para refletir com precisão os novos registros limpos do Firestore.
 */
data class EpisodeDto(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val number: Int = 0,

    @get:PropertyName("thumb_url")
    @set:PropertyName("thumb_url")
    var thumb_url: String = "",

    @get:PropertyName("video_url")
    @set:PropertyName("video_url")
    var video_url: String = "",

    @get:PropertyName("subtitle_url")
    @set:PropertyName("subtitle_url")
    var subtitle_url: String? = null
)