package io.android.discovery.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.android.discovery.state.DiscoveryState
import io.android.discovery.ui.components.*
import io.android.discovery.viewmodel.DiscoveryViewModel
import io.android.domain.algorithm.model.UserMetadata

@Composable
fun DiscoveryScreen(
    viewModel: DiscoveryViewModel = hiltViewModel(),
    onAnimeClick: (String) -> Unit,
    onPlayClick: (String, String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToWatchlist: () -> Unit // <-- Novo Callback
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val testUser = UserMetadata(
            userId = "dev_avenor_test",
            tagScores = mapOf("Ação" to 100, "Sci-Fi" to 50),
            watchHistory = emptyMap(),
            favoriteIds = emptyList(),
            lastSyncTimestamp = System.currentTimeMillis()
        )
        viewModel.loadDashboard(testUser)
    }

    Scaffold(
        bottomBar = {
            VintyBottomBar(
                onProfileClick = onNavigateToProfile,
                onWatchlistClick = onNavigateToWatchlist // <-- Conectado
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->

        when (val state = uiState) {
            is DiscoveryState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }

            is DiscoveryState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Erro ao carregar: ${state.message}",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is DiscoveryState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    state.heroAnime?.let { anime ->
                        item {
                            HeroCard(
                                imageUrl = anime.coverUrl,
                                title = anime.title,
                                matchScore = 98,
                                onSearchClick = { /* nada */ },
                                onNotifyClick = { /* nada */ },
                                onPlayClick = { onPlayClick(anime.id, "ep_1") }
                            )
                        }
                    }

                    if (state.continueWatching.isNotEmpty()) {
                        item {
                            SectionTitle(title = "Continue assistindo")
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(state.continueWatching) { item ->
                                    ContinueWatchingCard(
                                        imageUrl = item.anime.bannerUrl,
                                        progress = item.progress,
                                        onClick = { onPlayClick(item.anime.id, "last_pos") }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        SectionTitle(title = "Escolhidos a dedo")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.handPicked) { anime ->
                                VerticalAnimeCard(
                                    imageUrl = anime.coverUrl,
                                    title = anime.title,
                                    onClick = { onAnimeClick(anime.id) }
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        SectionTitle(title = "Você pode gostar")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.recommended) { anime ->
                                VerticalAnimeCard(
                                    imageUrl = anime.coverUrl,
                                    title = anime.title,
                                    onClick = { onAnimeClick(anime.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}