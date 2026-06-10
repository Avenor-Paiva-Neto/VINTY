package io.android.data.auth.di

import com.google.firebase.auth.FirebaseAuth
import io.android.data.auth.repository.AuthRepositoryImpl
import io.android.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    /**
     * O @Binds diz ao Hilt: "Sempre que pedirem AuthRepository,
     * use a implementação AuthRepositoryImpl".
     * É mais eficiente que @Provides pois não gera código extra de fábrica.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    companion object {
        /**
         * Como o FirebaseAuth vem de uma lib externa, não podemos usar @Inject.
         * Precisamos do @Provides aqui dentro do companion object.
         */
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}