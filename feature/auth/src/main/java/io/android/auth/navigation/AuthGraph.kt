package io.android.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.android.auth.ui.screens.AuthScreen
import io.android.auth.viewmodel.AuthViewModel
import io.android.core.navigation.Screen

/**
 * Função de extensão que "anexa" o fluxo de Auth no mapa mãe.
 */
fun NavGraphBuilder.authGraph(
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit
) {
    composable<Screen.Auth> {
        AuthScreen(
            viewModel = viewModel,
            onNavigateToHome = onNavigateToHome
        )
    }
}