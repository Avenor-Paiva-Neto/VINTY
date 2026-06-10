package io.android.data.progress.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.android.data.progress.repository.ProgressRepositoryImpl
import io.android.domain.player.repository.ProgressRepository
import javax.inject.Singleton

/**
 * Módulo Hilt para prover a injeção do ProgressRepository.
 * Utilizamos @Binds para associar a interface do Domínio com a implementação da camada de Dados.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ProgressModule {

    @Binds
    @Singleton
    abstract fun bindProgressRepository(
        progressRepositoryImpl: ProgressRepositoryImpl
    ): ProgressRepository
}