package io.android.data.auth.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.android.data.auth.mapper.AuthMapper
import io.android.domain.model.auth.AuthState
import io.android.domain.model.UserSession
import io.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementação robusta do AuthRepository utilizando Firebase.
 * @property firebaseAuth Instância do SDK do Firebase injetada via Hilt.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    override suspend fun loginWithGoogle(idToken: String): Result<UserSession> {
        return try {
            _authState.value = AuthState.Loading

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user

            if (user != null) {
                // No Firebase, buscamos o token atualizado após o login
                val tokenResult = user.getIdToken(true).await()
                val session = AuthMapper.toDomain(user, tokenResult.token ?: idToken)

                _authState.value = AuthState.Authenticated(session)
                Result.success(session)
            } else {
                val error = "Falha ao recuperar usuário do Firebase."
                _authState.value = AuthState.Error(error)
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Erro desconhecido")
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            _authState.value = AuthState.Unauthenticated
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkActiveSession() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            try {
                val tokenResult = currentUser.getIdToken(false).await()
                val session = AuthMapper.toDomain(currentUser, tokenResult.token ?: "")
                _authState.value = AuthState.Authenticated(session)
            } catch (e: Exception) {
                _authState.value = AuthState.Unauthenticated
            }
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
}