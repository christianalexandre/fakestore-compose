package com.christianalexandre.fakestore.presentation.feature_screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christianalexandre.fakestore.domain.repository.AuthRepository
import com.christianalexandre.fakestore.domain.repository.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data object OtpSent : LoginState
    data object LoggedIn : LoginState
    data class Error(val message: String) : LoginState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private var currentPhone = ""
    private var currentUuid = ""

    fun login(phone: String) {
        viewModelScope.launch {
            _state.update { LoginState.Loading }
            try {
                currentUuid = FirebaseMessaging.getInstance().token.await()
                currentPhone = phone

                when (val result = authRepository.login(phone, currentUuid)) {
                    is LoginResult.LoggedIn -> finishLogin(result.token)
                    is LoginResult.OtpRequired -> _state.update { LoginState.OtpSent }
                }
            } catch (e: Exception) {
                _state.update { LoginState.Error(e.message ?: "Erro ao conectar ao servidor") }
            }
        }
    }

    fun confirm(code: String) {
        viewModelScope.launch {
            _state.update { LoginState.Loading }
            try {
                val token = authRepository.confirm(currentPhone, currentUuid, code)
                finishLogin(token)
            } catch (e: Exception) {
                _state.update { LoginState.Error(e.message ?: "Código inválido") }
            }
        }
    }

    fun resetError() {
        _state.update { if (it is LoginState.Error) LoginState.OtpSent else LoginState.Idle }
    }

    private fun finishLogin(token: String) {
        _state.update { LoginState.LoggedIn }
        viewModelScope.launch {
            runCatching { firebaseAuth.signInAnonymously().await() }
        }
    }
}
