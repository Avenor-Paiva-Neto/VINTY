package io.craemza.watchlist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.android.domain.model.WatchlistItem

/**
 * Componente visual 100% Stateless (Burro) representando um item na Watchlist.
 * Padrão BMF: Não toma decisões. Apenas recebe os dados e emite intenções de clique (Callbacks).
 */
@Composable
fun WatchlistCard(
    item: WatchlistItem,
    onCardClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Espaçamento externo para a lista vertical
            .aspectRatio(0.8f) // Proporção de poster vertical
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant) // Cor de fundo caso a imagem demore a carregar
            .clickable { onCardClick() }
    ) {
        // Imagem de Fundo
        AsyncImage(
            model = item.coverUrl,
            contentDescription = "Capa de ${item.title}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradiente inferior para garantir o contraste do texto e do ícone
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Linha inferior contendo o Nome e o Botão de Favorito Preenchido
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f) // Ocupa o espaço disponível empurrando o ícone para o final
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Botão preenchido indicando que está salvo na lista
            IconButton(
                onClick = onRemoveClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary, // Cor de destaque do seu tema (ex: Vermelho ou Roxo)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Remover da Watchlist"
                )
            }
        }
    }
}