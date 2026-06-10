package io.android.player.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.android.domain.anime.model.Anime

@Composable
fun AnimeShowcaseHeader(
    anime: Anime,
    isWatchlisted: Boolean,
    onToggleWatchlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(0.8f) // Vertical idêntico ao seu exemplo
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1E1E2A))
    ) {
        // Imagem de Fundo
        AsyncImage(
            model = anime.coverUrl,
            contentDescription = "Capa de ${anime.title}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradiente inferior para destacar o Título e o Botão
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 600f
                    )
                )
        )

        // Linha inferior contendo o Título e o Botão de Favorito
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Apenas o título, com peso 1 para empurrar o botão para o canto
            Text(
                text = anime.title,
                modifier = Modifier.weight(1f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 2, // Previne que títulos imensos quebrem o layout
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Botão de Favorito (Adicionar/Remover da Watchlist)
            IconButton(
                onClick = onToggleWatchlist,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = if (isWatchlisted) MaterialTheme.colorScheme.primary else Color.White
                )
            ) {
                Icon(
                    imageVector = if (isWatchlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isWatchlisted) "Remover da Watchlist" else "Adicionar à Watchlist"
                )
            }
        }
    }
}