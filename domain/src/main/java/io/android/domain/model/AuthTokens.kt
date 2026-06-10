package io.android.domain.model

/**
 * Encapsula as credenciais de acesso.
 * Posicionado em io.android.domain.model para garantir consistência com UserSession.
 */
data class AuthTokens(
    val accessToken: String,
    val expirationTime: Long,
    val refreshToken: String? = null
) {
    fun isExpired(): Boolean {
        val safetyMargin = 5 * 60 * 1000
        return System.currentTimeMillis() > (expirationTime - safetyMargin)
    }
}