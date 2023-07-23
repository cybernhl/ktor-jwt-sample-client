package com.example.ktor_jwt_demo.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.ktor_jwt_demo.data.api.AuthApi
import com.example.ktor_jwt_demo.data.repo.AuthRepository
import com.example.ktor_jwt_demo.data.repo.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userData")

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    @Provides
    fun provideDataStoreRepository(
        dataStore: DataStore<Preferences>
    ): DataStoreRepository = DataStoreRepository(dataStore)

    @Provides
    @Singleton
    fun provideHttpClient(
        dataStoreRepository: DataStoreRepository
    ): HttpClient = HttpClient(CIO){
        install(ContentNegotiation){
            json()
        }

        engine {
            requestTimeout = 60_000L
        }

//        install(Auth) {
//            bearer {
//                loadTokens {
//                    BearerTokens(
//                        dataStoreRepository.getValue(DataStoreRepository.TOKEN) ?: "",
//                        "")
//                }
//            }
//        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor -> ", message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("Http status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            url("http://192.168.1.116:8080")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    @Provides
    fun provideAuthApi(
        client: HttpClient
    ): AuthApi = AuthApi(client)

    @Provides
    fun provideAuthRepository(
        api: AuthApi,
        dataStoreRepository: DataStoreRepository
    ): AuthRepository = AuthRepository(api, dataStoreRepository)
}