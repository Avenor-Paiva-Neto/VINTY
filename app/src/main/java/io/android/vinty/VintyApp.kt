package io.android.vinty

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Ponto de entrada base da aplicação.
 * A anotação @HiltAndroidApp dispara a geração de código do Hilt.
 */
@HiltAndroidApp
class VintyApp : Application()