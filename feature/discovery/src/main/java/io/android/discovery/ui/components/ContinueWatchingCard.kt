package io.android.discovery.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ContinueWatchingCard(
    imageUrl: String,
    progress: Float, // 0.0 a 1.0
    onClick: () -> Unit
) {
    // Box principal que contém o card e a pílula flutuante
    Box(
        modifier = Modifier
            .width(280.dp) // Largura fixa para o carrossel
            .height(180.dp)
            .padding(top = 12.dp) // Espaço reservado para a pílula subir
    ) {
        // 1. O Card de Vídeo
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp), // Empurra o card para baixo para encaixar a pílula
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Box(Modifier.fillMaxSize()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Barra de Progresso
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .align(Alignment.BottomCenter),
                    color = Color.Red,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
            }
        }

        // 2. A Pílula Flutuante (Floating Label)
        // Ela fica fora do Card, mas dentro da Box pai, alinhada ao topo
        Surface(
            color = Color(0xFF2A2A2A), // Cinza escuro
            shape = RoundedCornerShape(50), // Totalmente arredondada
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp) // Deslocamento lateral
                .height(28.dp) // Altura da pílula
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Continuar",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}