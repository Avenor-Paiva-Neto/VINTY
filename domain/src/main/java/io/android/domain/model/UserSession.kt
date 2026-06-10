package io.android.domain.model

/**
 * Representação da sessão ativa no ecossistema VINTY.
 * @param userId ID único gerado pelo provedor de autenticação (UID).
 * @param email Endereço de e-mail vinculado à conta Google.
 * @param displayName Nome público do usuário.
 * @param photoUrl Link para a imagem de perfil do Google.
 * @param tokens Objeto contendo os tokens de acesso e renovação.
 */
data class UserSession(
    val userId: String,
    val email: String,
    val displayName: String?,
    val photoUrl: String?,
    val tokens: AuthTokens
)

