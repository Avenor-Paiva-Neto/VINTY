package io.android.auth.orchestrator

import io.android.domain.model.auth.AuthState
import io.android.domain.repository.AuthRepository
import io.android.domain.usecase.auth.CheckActiveSessionUseCase
import io.android.domain.usecase.auth.LoginWithGoogleUseCase
import io.android.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Maestro do fluxo de Autenticação.
 * Centraliza a lógica de coordenação entre UseCases e expõe o estado global para a UI.
 */
class AuthOrchestrator @Inject constructor(
    private val loginUseCase: LoginWithGoogleUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val checkSessionUseCase: CheckActiveSessionUseCase,
    private val repository: AuthRepository // Para observar o authState reativo
) {

    /**
     * Observa o estado de autenticação que vem do coração do sistema (Repository/Domain).
     */
    val authState: StateFlow<AuthState> = repository.authState

    /**
     * Coordena o fluxo de entrada via Google.
     */
    suspend fun performLogin(idToken: String) {
        loginUseCase(idToken)
    }

    /**
     * Coordena a saída do usuário.
     */
    suspend fun performLogout() {
        logoutUseCase()
    }

    /**
     * Aciona a validação inicial de sessão.
     */
    suspend fun syncSession() {
        checkSessionUseCase()
    }
}