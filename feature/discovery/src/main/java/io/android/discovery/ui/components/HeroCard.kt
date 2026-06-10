package io.android.discovery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun HeroCard(
    imageUrl: String,
    title: String,
    matchScore: Int,
    onSearchClick: () -> Unit,
    onNotifyClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp) // Altura generosa para o destaque
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)) // Arredondamento inferior
    ) {
        // 1. Imagem de Fundo
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Gradiente Inferior (Legibilidade)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 300f
                    )
                )
        )

        // 3. Pílulas Superiores (Glassmorphism)
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp), // Espaçamento da StatusBar
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlassIconButton(icon = Icons.Default.Search, onClick = onSearchClick)
            GlassIconButton(icon = Icons.Default.Notifications, onClick = onNotifyClick)
        }

        // 4. Conteúdo de Texto Inferior
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text(
                text = "PARA VOCÊ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    letterSpacing = 1.sp
                ),
                color = Color.White
            )
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        // 5. Match Score (Canto Inferior Direito)
        Text(
            text = "$matchScore% para você",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Green, // Ou branco conforme o design
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
    }
}

@Composable
fun GlassIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.4f), // Glassmorphism simulado
        modifier = Modifier.size(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        }
    }
}