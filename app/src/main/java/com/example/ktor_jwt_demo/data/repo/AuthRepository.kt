package com.example.ktor_jwt_demo.data.repo

import com.example.ktor_jwt_demo.data.api.AuthApi
import com.example.ktor_jwt_demo.data.request.AuthRequest
import com.example.ktor_jwt_demo.data.request.SecretRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun signUp(request: AuthRequest): Flow<Result<String>> {
        return flow {
            api.signUp(request).onSuccess {
                signIn(request).collect {
                    emit(it)
                }
            }.onFailure {
                emit(Result.failure(it))
            }
        }
    }

    suspend fun signIn(request: AuthRequest): Flow<Result<String>> {
        return flow {
            api.signIn(request).onSuccess{
                dataStoreRepository.setValue(DataStoreRepository.TOKEN, it.token)
                dataStoreRepository.setValue(DataStoreRepository.USERNAME, it.username)
                dataStoreRepository.setValue(DataStoreRepository.USERID, it.userId)
                emit(Result.success("Success"))
            }.onFailure{
                emit(Result.failure(it))
            }
        }
    }

    suspend fun getSecret(request: SecretRequest): Flow<Result<String>> {
        return flow {
            api.secret(request).onSuccess {
                emit(Result.success(it))
            }.onFailure {
                emit(Result.failure(it))
            }
        }
    }
}