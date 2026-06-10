package io.android.domain.algorithm.model

/**
 * Constantes de peso para o cálculo do Score de Relevância.
 */
object AlgorithmConfig {
    const val WEIGHT_CLICK = 2
    const val WEIGHT_WATCH_10_PERCENT = 1
    const val WEIGHT_WATCH_90_PERCENT = 10
    const val WEIGHT_FAVORITE = 15

    // Percentual de animes aleatórios para quebrar a bolha (0.0 a 1.0)
    const val SERENDIPITY_FACTOR = 0.2
}