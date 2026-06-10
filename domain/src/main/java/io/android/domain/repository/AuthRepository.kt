package io.android.domain.repository

import io.android.domain.model.auth.AuthState
import io.android.domain.model.UserSession
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface de abstração para o sistema de Autenticação.
 * Segue o Dependency Inversion Principle (DIP) do SOLID.
 */

interface AuthRepository {

    /**
     * Exposição reativa do estado de autenticação para os componentes do sistema.
     */
    val authState: StateFlow<AuthState>

    /**
     * Realiza a autenticação utilizando o fluxo do Google Sign-In.
     * @param idToken Token de identidade fornecido pelo Google Play Services.
     */
    suspend fun loginWithGoogle(idToken: String): Result<UserSession>

    /**
     * Revoga os tokens e limpa a sessão local e remota.
     */
    suspend fun logout(): Result<Unit>

    /**
     * Sincroniza o estado local com o provedor de autenticação remoto.
     */
    suspend fun checkActiveSession()
}