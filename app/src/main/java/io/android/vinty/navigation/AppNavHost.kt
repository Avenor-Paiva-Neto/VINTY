package io.android.vinty.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.android.auth.navigation.authGraph
import io.android.auth.viewmodel.AuthViewModel
import io.android.core.navigation.Screen
import io.android.profile.ui.screens.ProfileScreen
import io.android.profile.viewmodel.ProfileViewModel
import io.android.discovery.ui.DiscoveryScreen
import io.android.discovery.viewmodel.DiscoveryViewModel
import io.android.player.ui.screens.PlayerScreen
import io.android.player.viewmodel.PlayerViewModel
import io.android.player.provider.PlayerProvider
import io.craemza.watchlist.ui.screens.WatchlistScreen
import io.craemza.watchlist.viewmodel.WatchlistViewModel

@Composable
fun AppNavHost(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Auth
    ) {
        // 1. Módulo de Autenticação
        authGraph(
            viewModel = authViewModel,
            onNavigateToHome = {
                navController.navigate(Screen.Discovery) {
                    popUpTo(Screen.Auth) { inclusive = true }
                }
            }
        )

        // 2. Módulo Discovery
        composable<Screen.Discovery> {
            val discoveryViewModel: DiscoveryViewModel = hiltViewModel()

            DiscoveryScreen(
                viewModel = discoveryViewModel,
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.Player(animeId = animeId))
                },
                onPlayClick = { animeId, episodeId ->
                    navController.navigate(Screen.Player(animeId, episodeId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                onNavigateToWatchlist = {
                    navController.navigate(Screen.Watchlist)
                }
            )
        }

        // 3. Módulo Watchlist
        composable<Screen.Watchlist> {
            val watchlistViewModel: WatchlistViewModel = hiltViewModel()

            WatchlistScreen(
                viewModel = watchlistViewModel,
                onNavigateToPlayer = { animeId ->
                    navController.navigate(Screen.Player(animeId = animeId))
                }
            )
        }

        // 4. Módulo Player
        composable<Screen.Player> { backStackEntry ->
            val route: Screen.Player = backStackEntry.toRoute()
            val playerViewModel: PlayerViewModel = hiltViewModel()

            PlayerScreen(
                animeId = route.animeId,
                viewModel = playerViewModel,
                playerProvider = playerViewModel.playerProvider
            )
        }

        // 5. Módulo Profile
        composable<Screen.Profile> {
            val profileViewModel: ProfileViewModel = hiltViewModel()

            ProfileScreen(
                viewModel = profileViewModel,
                onLogoutSuccess = {
                    navController.navigate(Screen.Auth) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}