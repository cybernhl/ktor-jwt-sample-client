package com.example.ktor_jwt_demo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktor_jwt_demo.data.repo.AuthRepository
import com.example.ktor_jwt_demo.data.request.AuthRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _state: MutableStateFlow<AuthState> = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _authResult = Channel<Result<String>>()
    val authResult = _authResult.receiveAsFlow()

    fun onUsernameChange(username: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(username = username)
            }
        }
    }

    fun onPasswordChange(password: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(password = password)
            }
        }
    }

    fun switchMode() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    title = if (it.title == "登入") "注册" else "登入",
                    switchButtonText = if (it.title == "登入") "登入" else "注册",
                )
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            val request = AuthRequest(
                username = _state.value.username,
                password = _state.value.password
            )
            authRepository.signUp(request).collect{
                _authResult.send(it)
            }
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val request = AuthRequest(
                username = _state.value.username,
                password = _state.value.password
            )
            authRepository.signIn(request).collect{
                _authResult.send(it)
            }
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }
}