package io.android.profile.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Modal de confirmação para evitar saídas acidentais.
 */
@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Sair da conta")
        },
        text = {
            Text(text = "Tem certeza de que deseja desconectar sua sessão atual?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Sim, sair")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}