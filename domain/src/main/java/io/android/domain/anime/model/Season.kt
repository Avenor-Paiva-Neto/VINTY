package io.android.domain.anime.model

// Modelo de Temporada
data class Season(
    val id: String,
    val title: String,
    val audioType: String, // DUBLADO ou LEGENDADO
    val driveFolderId: String,
    val defaultThumbUrl: String,
    val episodeCount: Int
)

/**
 * Modelo de Episódio
 * BMF: Atualizado para refletir a nova estrutura limpa do Firestore.
 */
data class Episode(
    val id: String,
    val title: String,
    val number: Long,       // Substituiu o index para se alinhar ao banco
    val videoUrl: String,   // Armazena o ID bruto do Drive para o streaming
    val duration: String,   // Adicionado para o tempo de reprodução
    val thumbUrl: String,
    val subtitleUrl: String? = null // Adicionado como nulo por padrão para suportar legendas
)