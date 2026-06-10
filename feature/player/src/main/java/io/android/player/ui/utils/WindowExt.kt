package io.android.player.ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Extensão para localizar a Activity atual a partir do Contexto do Compose.
 * Necessário porque o LocalContext.current pode retornar um ContextWrapper.
 */
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/**
 * Alterna a Activity para o modo tela cheia (paisagem) e oculta as barras do sistema,
 * ou retorna para o modo retrato com as barras visíveis.
 *
 * @param context O contexto atual capturado no Compose (LocalContext.current).
 * @param isFullScreen True para deitar a tela e esconder barras, False para restaurar.
 */
fun toggleFullScreen(context: Context, isFullScreen: Boolean) {
    val activity = context.findActivity() ?: return
    val window = activity.window

    // WindowCompat garante retrocompatibilidade com versões mais antigas do Android
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)

    if (isFullScreen) {
        // Deita a tela usando os sensores do aparelho (permite virar para ambos os lados em landscape)
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        // Define o comportamento para que as barras apareçam temporariamente se o usuário deslizar os dedos nas bordas
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Oculta as barras de status (topo) e navegação (botões/gestos em baixo)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
    } else {
        // Devolve o controle de orientação para o sistema (geralmente Retrato travado ou sensor normal)
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        // Exibe as barras do sistema novamente
        insetsController.show(WindowInsetsCompat.Type.systemBars())
    }
}