package io.android.profile.state

/**
 * Representa o estado imutável da tela de Perfil.
 * Focado estritamente em dados de UI (Driving Adapter).
 */
sealed interface ProfileUiState {

    /**
     * Estado inicial ou durante processamentos assíncronos (ex: efetuando logout).
     */
    data object Loading : ProfileUiState
    data object Unauthenticated : ProfileUiState

    /**
     * Estado de sucesso com os dados do usuário mapeados da sessão.
     * * @param displayName Nome público do usuário.
     * @param email E-mail vinculado à conta.
     * @param photoUrl URL da imagem de perfil (pode ser nulo).
     * @param accountStatus Status da conta no sistema, padronizado com as regras de domínio.
     * @param showLogoutDialog Sinalizador para exibir ou ocultar o modal de confirmação de logout.
     */
    data class Success(
        val displayName: String,
        val email: String,
        val photoUrl: String?,
        val accountStatus: String = "VERIFICADO",
        val showLogoutDialog: Boolean = false
    ) : ProfileUiState

    /**
     * Estado de falha, repassando mensagens de erro para a UI (ex: falha ao deslogar).
     */
    data class Error(val message: String) : ProfileUiState
}