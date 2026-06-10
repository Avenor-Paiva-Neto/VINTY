package io.android.data.watchlist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.android.data.watchlist.repository.WatchlistRepositoryImpl
import io.android.domain.repository.WatchlistRepository
import javax.inject.Singleton

/**
 * Módulo Hilt responsável por prover as dependências da feature de Watchlist.
 * Padrão BMF: Instalação no SingletonComponent para garantir uma única instância do repositório
 * durante todo o ciclo de vida da aplicação.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class WatchlistModule {

    /**
     * Ensina ao Hilt qual implementação concreta usar quando uma interface do Domínio for solicitada.
     */
    @Binds
    @Singleton
    abstract fun bindWatchlistRepository(
        watchlistRepositoryImpl: WatchlistRepositoryImpl
    ): WatchlistRepository
}