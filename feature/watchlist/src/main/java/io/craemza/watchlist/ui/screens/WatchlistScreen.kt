package io.craemza.watchlist.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.android.domain.model.WatchlistItem
import io.craemza.watchlist.presentation.state.WatchlistUiEvent
import io.craemza.watchlist.presentation.state.WatchlistUiState
import io.craemza.watchlist.ui.components.WatchlistCard
import io.craemza.watchlist.viewmodel.WatchlistViewModel

/**
 * Tela principal (Stateful) da feature de Watchlist.
 * Observa o fluxo unidirecional (UDF) e despacha eventos e navegações.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    onNavigateToPlayer: (animeId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // Estado local para controlar a exibição do diálogo de confirmação
    var itemToRemove by remember { mutableStateOf<WatchlistItem?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Minha Lista") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is WatchlistUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                is WatchlistUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }

                is WatchlistUiState.Unauthenticated -> {
                    Text(
                        text = "Você precisa estar logado para ver sua lista.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }

                is WatchlistUiState.Success -> {
                    if (state.items.isEmpty()) {
                        Text(
                            text = "Sua lista está vazia. Explore o catálogo e adicione suas obras favoritas!",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.items,
                                key = { it.animeId } // Otimiza a performance da lista ao reordenar/deletar
                            ) { animeItem ->
                                WatchlistCard(
                                    item = animeItem,
                                    onCardClick = {
                                        viewModel.onEvent(WatchlistUiEvent.OnAnimeClick(animeItem.animeId))
                                        onNavigateToPlayer(animeItem.animeId)
                                    },
                                    onRemoveClick = {
                                        itemToRemove = animeItem // Dispara a exibição do diálogo
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmação para remoção
    itemToRemove?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToRemove = null },
            title = { Text(text = "Remover da lista") },
            text = { Text(text = "Tem certeza que deseja remover '${item.title}' da sua Watchlist?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(WatchlistUiEvent.OnRemoveClick(item.animeId))
                        itemToRemove = null
                    }
                ) {
                    Text("Remover", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToRemove = null }) {
                    Text("Cancelar")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}