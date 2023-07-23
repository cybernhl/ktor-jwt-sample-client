package com.example.ktor_jwt_demo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktor_jwt_demo.data.repo.AuthRepository
import com.example.ktor_jwt_demo.data.repo.DataStoreRepository
import com.example.ktor_jwt_demo.data.request.SecretRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _result = Channel<Result<String>>()
    val result = _result.receiveAsFlow()

    fun getSecret() {
        viewModelScope.launch {
            val token = dataStoreRepository.getValue(DataStoreRepository.TOKEN)
            if (token == null) {
                _result.send(Result.failure(Exception("Token not found.")))
                return@launch
            }
            val request = SecretRequest(token)
            authRepository.getSecret(request).collect{
                it.onSuccess {
                    _state.value = _state.value.copy(secret = it)
                }.onFailure {
                    _result.send(Result.failure(it))
                }
            }
        }
    }

    fun getSecretWithoutToken() {
        viewModelScope.launch {
            val request = SecretRequest("")
            authRepository.getSecret(request).collect{
                _result.send(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreRepository.clear()
        }
    }
}