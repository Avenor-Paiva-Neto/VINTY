package io.android.player.logic

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Rastreia o progresso de reprodução em tempo real.
 * Utiliza @Inject para permitir a injeção via Hilt no PlayerViewModel.
 */
class PlaybackTracker @Inject constructor() {

    /**
     * Emite o percentual assistido baseado na posição real do Player.
     * * @param getCurrentPosition Função que retorna a posição atual (ms) do player.
     * @param getDuration Função que retorna a duração total (ms) do player.
     * @param intervalMs Intervalo de amostragem (padrão 2000ms para evitar escrita excessiva no Firebase).
     */
    fun track(
        getCurrentPosition: () -> Long,
        getDuration: () -> Long,
        intervalMs: Long = 2000L
    ) = flow {
        while (true) {
            delay(intervalMs)

            val duration = getDuration()
            val current = getCurrentPosition()

            // Só emitimos se a duração for válida (evita divisão por zero)
            if (duration > 0) {
                val percent = current.toFloat() / duration.toFloat()

                // Emitimos o percentual calculado
                emit(percent)
            }
        }
    }
}