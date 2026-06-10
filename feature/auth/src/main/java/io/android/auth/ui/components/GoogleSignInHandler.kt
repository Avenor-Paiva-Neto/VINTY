package io.android.auth.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

/**
 * Componente funcional para gerenciar o contrato de resultado do Google Sign-In.
 * @param onTokenReceived Emitido quando o ID Token é validado com sucesso.
 * @param onSignInError Emitido em caso de falha técnica ou cancelamento.
 * @param triggerLogin Quando verdadeiro, dispara o pop-up do Google.
 */
@Composable
fun GoogleSignInHandler(
    triggerLogin: Boolean,
    onTokenReceived: (String) -> Unit,
    onSignInError: (String) -> Unit,
    onResetTrigger: () -> Unit
) {
    val context = LocalContext.current

    // Configuração Expressive: Solicitamos o ID Token para o Firebase
    // io.android.auth.ui.components.GoogleSignInHandler.kt

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("823023450880-169m8j876tp1129ojs7i065tk73s1pns.apps.googleusercontent.com") // Substituído aqui
        .requestEmail()
        .requestProfile()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                onTokenReceived(idToken)
            } else {
                onSignInError("ID Token nulo: Verifique as configurações do Google Console.")
            }
        } catch (e: ApiException) {
            onSignInError("Erro Google (${e.statusCode}): ${e.localizedMessage}")
        }
        onResetTrigger()
    }

    // Dispara o launcher apenas quando o estado externo solicitar
    if (triggerLogin) {
        SideEffect {
            launcher.launch(googleSignInClient.signInIntent)
        }
    }
}