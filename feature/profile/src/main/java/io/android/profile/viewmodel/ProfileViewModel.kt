package io.android.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.android.domain.model.auth.AuthState
import io.android.domain.repository.AuthRepository
import io.android.domain.usecase.auth.LogoutUseCase
import io.android.profile.state.ProfileUiEvent
import io.android.profile.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Maestro da tela de Perfil.
 * Orquestra o estado da UI de forma reativa e processa as intenções do usuário.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    /**
     * Observa passivamente o estado global de autenticação mantido pelo domínio.
     * Transforma as regras de negócio em estados puramente visuais para a UI.
     */
    private fun observeAuthState() {
        authRepository.authState.onEach { authState ->
            when (authState) {
                is AuthState.Authenticated -> {
                    _uiState.update { currentState ->
                        // Preserva o estado do dialog caso ele já esteja aberto durante uma recomposição
                        val isDialogShowing = (currentState as? ProfileUiState.Success)?.showLogoutDialog ?: false

                        ProfileUiState.Success(
                            displayName = authState.session.displayName ?: "Usuário Vinty",
                            email = authState.session.email,
                            photoUrl = authState.session.photoUrl,
                            accountStatus = "VERIFICADO", // Nomenclatura oficial garantida
                            showLogoutDialog = isDialogShowing
                        )
                    }
                }
                is AuthState.Loading -> {
                    _uiState.value = ProfileUiState.Loading
                }
                is AuthState.Error -> {
                    _uiState.value = ProfileUiState.Error(authState.message)
                }
                is AuthState.Idle, is AuthState.Unauthenticated -> {
                    // Quando deslogado, a UI aguarda (Loading) enquanto o roteador global
                    // do sistema detecta o 'Unauthenticated' e ejeta o usuário da rota protegida.
                    _uiState.value = ProfileUiState.Loading
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Ponto de entrada único para ações vindas da UI (UDF).
     */
    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnLogoutClicked -> {
                updateSuccessState { it.copy(showLogoutDialog = true) }
            }
            is ProfileUiEvent.OnDismissLogoutDialog -> {
                updateSuccessState { it.copy(showLogoutDialog = false) }
            }
            is ProfileUiEvent.OnConfirmLogout -> {
                updateSuccessState { it.copy(showLogoutDialog = false) }
                performLogout()
            }
        }
    }

    /**
     * Função auxiliar para atualizar o estado apenas se a tela estiver carregada (Success).
     */
    private fun updateSuccessState(update: (ProfileUiState.Success) -> ProfileUiState.Success) {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success) {
            _uiState.value = update(currentState)
        }
    }

    /**
     * Executa a regra de negócio para encerramento de sessão.
     */
    private fun performLogout() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            val result = logoutUseCase()

            result.onFailure { exception ->
                _uiState.value = ProfileUiState.Error(
                    message = exception.message ?: "Ocorreu um erro ao tentar sair da conta."
                )
            }
            // Em caso de sucesso, o repositório emitirá AuthState.Unauthenticated.
            // O collector 'observeAuthState' receberá isso e o controle passará para a navegação raiz.
        }
    }
}