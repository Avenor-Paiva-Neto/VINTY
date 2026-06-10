package io.android.discovery.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold, // Peso conforme seu design
            fontSize = 20.sp // Tamanho legível (20px a 22px)
        ),
        color = Color.White, // Branco para fundo Dark
        modifier = Modifier
            .padding(start = 16.dp, top = 24.dp, bottom = 12.dp) // Espaçamento padrão
    )
}