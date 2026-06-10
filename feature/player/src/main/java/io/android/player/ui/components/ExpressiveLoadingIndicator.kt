package io.android.player.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

/**
 * Indicador de carregamento Expressive (Material 3).
 * Muda de forma (morphing) de um squircle para um círculo enquanto rotaciona.
 */
@Composable
fun ExpressiveLoadingIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "expressive_loader")

    // Gira continuamente 360 graus
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loader_rotation"
    )

    // Animação de Morphing: Começa com bordas em 15% e vai até 50% (círculo perfeito)
    val cornerRadius by infiniteTransition.animateFloat(
        initialValue = 15f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loader_morphing"
    )

    Box(
        modifier = modifier
            .size(56.dp) // Tamanho um pouco maior para destaque sobre o vídeo
            .rotate(rotation)
            .clip(RoundedCornerShape(percent = cornerRadius.toInt()))
            .background(MaterialTheme.colorScheme.primary)
    )
}