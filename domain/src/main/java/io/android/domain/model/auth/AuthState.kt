package io.android.domain.model.auth

import io.android.domain.model.UserSession
/**
 * Estados da máquina de autenticação.
 * Usado pelo ViewModel para emitir estados reativos à UI.
 */
sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Authenticated(val session: UserSession) : AuthState()
    data object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}