package io.android.profile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.android.profile.state.ProfileUiEvent
import io.android.profile.state.ProfileUiState
import io.android.profile.ui.components.LogoutButton
import io.android.profile.ui.components.LogoutConfirmationDialog
import io.android.profile.ui.components.ProfileHeader
import io.android.profile.viewmodel.ProfileViewModel

/**
 * Ponto de entrada da tela de Perfil.
 * Gerencia a subscrição ao estado do ViewModel e o roteamento de eventos (State Hoisting).
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutSuccess: () -> Unit
) {
    // Coleta o estado de forma consciente ao ciclo de vida (AndroidX Lifecycle)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Unauthenticated) { // Você precisará adicionar esse estado
            onLogoutSuccess()
        }
    }
    ProfileContent(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}

/**
 * Versão Stateless da tela, facilitando testes e previews.
 */
@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }

                is ProfileUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }

                is ProfileUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Componente de Cabeçalho (Avatar + Dados)
                        ProfileHeader(
                            displayName = state.displayName,
                            email = state.email,
                            photoUrl = state.photoUrl,
                            accountStatus = state.accountStatus
                        )

                        // Empurra o botão para a parte inferior da tela
                        Spacer(modifier = Modifier.weight(1f))

                        // Componente do Botão de Logout
                        LogoutButton(
                            onClick = { onEvent(ProfileUiEvent.OnLogoutClicked) },
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                    }

                    // Controle do Diálogo de Confirmação
                    if (state.showLogoutDialog) {
                        LogoutConfirmationDialog(
                            onConfirm = { onEvent(ProfileUiEvent.OnConfirmLogout) },
                            onDismiss = { onEvent(ProfileUiEvent.OnDismissLogoutDialog) }
                        )
                    }
                }


                is ProfileUiState.Unauthenticated -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}