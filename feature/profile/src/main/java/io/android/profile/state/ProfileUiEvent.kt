package io.android.profile.state

/**
 * Mapeamento das intenções de ação do usuário na tela de Perfil.
 * Garante o fluxo unidirecional de dados (UDF).
 */
sealed interface ProfileUiEvent {
    /**
     * Disparado quando o usuário clica no botão principal de "Sair".
     */
    data object OnLogoutClicked : ProfileUiEvent

    /**
     * Disparado quando o usuário confirma o desejo de sair dentro do modal.
     */
    data object OnConfirmLogout : ProfileUiEvent

    /**
     * Disparado quando o usuário cancela o logout ou clica fora do modal.
     */
    data object OnDismissLogoutDialog : ProfileUiEvent
}