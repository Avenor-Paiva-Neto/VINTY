package io.android.data.anime.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

/**
 * DTO para representar a Obra no Firestore.
 * @IgnoreExtraProperties evita os warnings de campos como 'updated_at' no Logcat.
 */
@IgnoreExtraProperties
data class AnimeDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",

    @get:PropertyName("cover_url")
    @set:PropertyName("cover_url")
    var cover_url: String = "",

    @get:PropertyName("banner_url")
    @set:PropertyName("banner_url")
    var banner_url: String = "",

    val tags: List<String> = emptyList(),

    @get:PropertyName("relevance_score")
    @set:PropertyName("relevance_score")
    var relevance_score: Int = 0,

    @get:PropertyName("content_rating")
    @set:PropertyName("content_rating")
    var content_rating: String = "",

    @get:PropertyName("release_year")
    @set:PropertyName("release_year")
    var release_year: Int = 0,

    val studio: String = "",

    @get:PropertyName("random_seed")
    @set:PropertyName("random_seed")
    var random_seed: Int = 0



)