package io.android.auth.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.android.auth.ui.components.GoogleSignInHandler
import io.android.auth.viewmodel.AuthViewModel
import io.android.domain.model.auth.AuthState

/**
 * AuthScreen: A porta de entrada do VINTY.
 * Design focado em Material 3 Expressive com suporte a tons Mushroom.
 */
@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var startGoogleSignIn by remember { mutableStateOf(false) }

    // Gerenciador de Snackbars para o padrão M3
    val snackbarHostState = remember { SnackbarHostState() }

    // Observador de Efeitos e Navegação
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthState.Authenticated -> onNavigateToHome()
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> {} // Ignora Loading e Idle
        }
    }

    // Injeção do Handler de Google (Invisível, apenas lógica)
    // Restaurado exatamente como você mapeou.
    GoogleSignInHandler(
        triggerLogin = startGoogleSignIn,
        onTokenReceived = { token -> viewModel.onLoginRequested(token) },
        onSignInError = { error -> /* Pode ser enviado para um UiEffect futuramente */ },
        onResetTrigger = { startGoogleSignIn = false }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Branding Expressivo: VINTY
                Text(
                    text = "VINTY",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-2.5).sp,
                        lineHeight = 64.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Seu museu particular de animes.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))

                // Botão de Ação: Estilo Expressive e Responsivo
                Button(
                    onClick = { startGoogleSignIn = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    enabled = uiState !is AuthState.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                ) {
                    // Transição Suave entre Loading e Texto
                    AnimatedContent(
                        targetState = uiState is AuthState.Loading,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                        },
                        label = "GoogleSignInButtonState"
                    ) { isLoading ->
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 3.dp,
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Entrar com Google",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}