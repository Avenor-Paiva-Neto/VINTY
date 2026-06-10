package io.android.player.provider

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var exoPlayer: ExoPlayer? = null

    // BMF: Guarda a URL atual para evitar recarregamento duplo
    private var currentPlayingUrl: String? = null

    @OptIn(UnstableApi::class)
    fun getPlayer(): ExoPlayer {
        if (exoPlayer == null) {
            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .setAllowCrossProtocolRedirects(true)

            val mediaSourceFactory = DefaultMediaSourceFactory(context)
                .setDataSourceFactory(httpDataSourceFactory)

            exoPlayer = ExoPlayer.Builder(context)
                .setMediaSourceFactory(mediaSourceFactory)
                .setSeekBackIncrementMs(10000L)
                .setSeekForwardIncrementMs(10000L)
                .build()
        }
        return exoPlayer!!
    }

    fun prepareAndPlay(videoUrl: String, subtitleUrl: String? = null) {
        if (videoUrl.isBlank()) return

        // BMF: Se o vídeo solicitado for o mesmo que já está tocando, ignoramos a re-injeção.
        // Isso impede que a tela sobrescreva a legenda com 'null' num duplo disparo.
        if (videoUrl == currentPlayingUrl) {
            println("BMF LOG - Ignorando chamada repetida da UI para o mesmo vídeo.")
            return
        }

        currentPlayingUrl = videoUrl // Salva a URL como a que está rodando agora

        val player = getPlayer()
        val mediaItemBuilder = MediaItem.Builder().setUri(videoUrl)

        if (!subtitleUrl.isNullOrBlank()) {
            val subtitleConfig = MediaItem.SubtitleConfiguration.Builder(Uri.parse(subtitleUrl))
                .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                .setLanguage("pt")
                .setSelectionFlags(C.SELECTION_FLAG_DEFAULT or C.SELECTION_FLAG_FORCED)
                .build()

            mediaItemBuilder.setSubtitleConfigurations(listOf(subtitleConfig))
        }

        player.setMediaItem(mediaItemBuilder.build())
        player.prepare()
        player.playWhenReady = true
    }

    @OptIn(UnstableApi::class)
    fun setPlaybackSpeed(speed: Float) {
        exoPlayer?.playbackParameters = PlaybackParameters(speed)
    }

    fun releasePlayer() {
        exoPlayer?.let {
            it.stop()
            it.release()
        }
        exoPlayer = null
        currentPlayingUrl = null // Limpa a URL armazenada ao destruir o player
    }
}