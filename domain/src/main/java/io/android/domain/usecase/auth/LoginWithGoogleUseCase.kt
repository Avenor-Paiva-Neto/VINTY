package io.android.domain.usecase.auth

import io.android.domain.model.UserSession
import io.android.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de uso responsável por orquestrar o login via Google.
 * Segue o Princípio da Responsabilidade Única (SRP).
 */
class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Executa a lógica de autenticação.
     * @param idToken O token gerado pelo SDK do Google Sign-In no lado da UI.
     * @return Result contendo a [UserSession] em caso de sucesso.
     */
    suspend operator fun invoke(idToken: String): Result<UserSession> {
        return if (idToken.isBlank()) {
            Result.failure(IllegalArgumentException("O ID Token não pode estar vazio."))
        } else {
            repository.loginWithGoogle(idToken)
        }
    }
}