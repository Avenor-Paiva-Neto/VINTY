package io.android.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.android.auth.orchestrator.AuthOrchestrator
import io.android.domain.model.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsável por gerenciar o estado da UI de Autenticação.
 * Atua como uma Proxy entre o [AuthOrchestrator] e o Jetpack Compose.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val orchestrator: AuthOrchestrator
) : ViewModel() {

    /**
     * Expõe o estado de autenticação de forma quente (hot stream) para a UI.
     * O estado é mantido enquanto houver assinantes, com um timeout de 5s.
     */
    val uiState: StateFlow<AuthState> = orchestrator.authState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.Idle
        )

    /**
     * Gatilho para o login via Google.
     * @param idToken Token obtido através do Google Sign-In no Compose.
     */
    fun onLoginRequested(idToken: String) {
        viewModelScope.launch {
            orchestrator.performLogin(idToken)
        }
    }

    /**
     * Gatilho para logout.
     */
    fun onLogoutRequested() {
        viewModelScope.launch {
            orchestrator.performLogout()
        }
    }

    /**
     * Chamado na inicialização ou no Splash para validar a sessão.
     */
    fun checkSession() {
        viewModelScope.launch {
            orchestrator.syncSession()
        }
    }
}