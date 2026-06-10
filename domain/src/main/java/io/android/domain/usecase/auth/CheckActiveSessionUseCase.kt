package io.android.domain.usecase.auth

import io.android.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de uso responsável por verificar e validar a sessão existente ao iniciar o app.
 * Garante que o estado de autenticação esteja sincronizado com o provedor (Firebase).
 */
class CheckActiveSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Aciona a verificação de sessão no repositório.
     * O resultado será refletido no StateFlow 'authState' do repositório.
     */
    suspend operator fun invoke() {
        repository.checkActiveSession()
    }
}