package io.android.player.ui.components

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import io.android.player.provider.PlayerProvider

@OptIn(UnstableApi::class)
@Composable
fun VintyVideoPlayer(
    videoUrl: String,
    playerProvider: PlayerProvider,
    isDatabaseLoading: Boolean = false,
    isFullScreen: Boolean = false,
    onFullScreenToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoPlayer = playerProvider.getPlayer()

    var isNetworkBuffering by remember { mutableStateOf(false) }

    // 1. Monitora o ciclo de vida E o estado de rede do ExoPlayer
    DisposableEffect(lifecycleOwner, exoPlayer) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                // Apenas pausamos e damos play para economizar bateria em background.
                // Removemos o ON_DESTROY daqui. Quem mata o player agora é o ViewModel!
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        val playerListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isNetworkBuffering = playbackState == Player.STATE_BUFFERING
            }
        }
        exoPlayer.addListener(playerListener)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            exoPlayer.removeListener(playerListener)
        }
    }

    // 2. Prepara e carrega o vídeo sempre que a URL mudar
    LaunchedEffect(videoUrl) {
        if (videoUrl.isNotEmpty()) {
            playerProvider.prepareAndPlay(videoUrl)
        }
    }

    // 3. Estrutura de Sobreposição
    Box(modifier = modifier.background(Color.Black)) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true
                    setShowNextButton(false)
                    setShowPreviousButton(false)

                    setFullscreenButtonClickListener { isFull ->
                        onFullScreenToggle(isFull)
                    }
                }
            }
        )

        val showLoader = isDatabaseLoading || isNetworkBuffering || videoUrl.isEmpty()

        AnimatedVisibility(
            visible = showLoader,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            ExpressiveLoadingIndicator()
        }
    }
}