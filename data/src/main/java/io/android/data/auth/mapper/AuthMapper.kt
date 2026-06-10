package io.android.data.auth.mapper

import com.google.firebase.auth.FirebaseUser
import io.android.domain.model.AuthTokens
import io.android.domain.model.UserSession

object AuthMapper {

    fun toDomain(firebaseUser: FirebaseUser, idToken: String): UserSession {
        return UserSession(
            userId = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName,
            photoUrl = firebaseUser.photoUrl?.toString(),
            tokens = AuthTokens(
                accessToken = idToken,
                // O Firebase geralmente emite tokens de 1 hora (3600 segundos)
                expirationTime = System.currentTimeMillis() + (3600 * 1000)
            )
        )
    }
}