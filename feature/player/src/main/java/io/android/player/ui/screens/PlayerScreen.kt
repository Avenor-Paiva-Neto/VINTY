package io.android.player.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.android.domain.player.model.PlaybackState
import io.android.player.provider.PlayerProvider
import io.android.player.ui.components.AnimeShowcaseHeader
import io.android.player.ui.components.ExpandableDescription
import io.android.player.ui.components.SeasonAccordion
import io.android.player.ui.components.VintyVideoPlayer
import io.android.player.ui.utils.toggleFullScreen
import io.android.player.viewmodel.PlayerUiState
import io.android.player.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(
    animeId: String,
    viewModel: PlayerViewModel,
    playerProvider: PlayerProvider,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()

    // BMF: Observa o estado da Watchlist em tempo real
    val isWatchlisted by viewModel.isWatchlisted.collectAsState()

    val context = LocalContext.current
    var isFullScreenVideo by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(animeId) { viewModel.loadAnimeDetails(animeId) }
    LaunchedEffect(isFullScreenVideo) { toggleFullScreen(context, isFullScreenVideo) }

    BackHandler(enabled = isFullScreenVideo) {
        isFullScreenVideo = false
        playerProvider.getPlayer().pause()
    }

    if (isFullScreenVideo) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            val activeVideoUrl = viewModel.getCurrentUrl() ?: ""
            VintyVideoPlayer(
                videoUrl = activeVideoUrl,
                playerProvider = playerProvider,
                isDatabaseLoading = playbackState is PlaybackState.Buffering,
                isFullScreen = true,
                onFullScreenToggle = { isFullScreenVideo = false; playerProvider.getPlayer().pause() },
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Scaffold(modifier = modifier.fillMaxSize(), containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
            val state = uiState

            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                when (state) {
                    is PlayerUiState.Success -> {
                        val anime = state.details.anime
                        val seasons = state.details.seasons
                        var expandedSeasonId by remember { mutableStateOf(seasons.firstOrNull()?.season?.id ?: "") }

                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                // BMF: Passagem de estado e evento reativo ao componente de cabeçalho
                                AnimeShowcaseHeader(
                                    anime = anime,
                                    isWatchlisted = isWatchlisted,
                                    onToggleWatchlist = { viewModel.toggleWatchlist() }
                                )
                            }

                            item {
                                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                    ExpandableDescription(description = anime.description)
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(text = "Temporadas", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                                }
                            }

                            items(seasons) { seasonWithEps ->
                                SeasonAccordion(
                                    seasonWithEpisodes = seasonWithEps,
                                    isExpanded = expandedSeasonId == seasonWithEps.season.id,
                                    onExpandClick = {
                                        expandedSeasonId = if (expandedSeasonId == seasonWithEps.season.id) "" else seasonWithEps.season.id
                                        viewModel.onExpandSeason(anime.id, seasonWithEps.season.id)
                                    },
                                    onEpisodeClick = { episodeId ->
                                        viewModel.onPlayEpisode(anime.id, seasonWithEps.season.id, episodeId)
                                        isFullScreenVideo = true
                                    }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(32.dp)) }
                        }
                    }
                    is PlayerUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is PlayerUiState.Error -> {
                        Text(
                            text = state.message,
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}