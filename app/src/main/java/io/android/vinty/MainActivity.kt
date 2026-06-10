package io.android.vinty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.android.core.ui.theme.VINTYTheme
import io.android.auth.viewmodel.AuthViewModel
import io.android.vinty.navigation.AppNavHost // Importe o seu NavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Usando ktx viewModels para garantir que o Hilt proveja a instância
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Verifica se já existe um usuário logado no Firebase ao iniciar
        authViewModel.checkSession()

        setContent {
            VINTYTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // MUDANÇA CHAVE: Chamamos o NavHost em vez de uma tela fixa.
                    // Ele será o responsável por coordenar Auth -> Discovery.
                    AppNavHost(authViewModel = authViewModel)
                }
            }
        }
    }
}