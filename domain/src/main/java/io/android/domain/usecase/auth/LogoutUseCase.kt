package io.android.domain.usecase.auth

import io.android.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de uso responsável por encerrar a sessão do usuário.
 * Limpa credenciais e estados de autenticação em todo o sistema.
 */
class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Executa o encerramento da sessão.
     * @return Result indicando se o logout foi bem-sucedido.
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}