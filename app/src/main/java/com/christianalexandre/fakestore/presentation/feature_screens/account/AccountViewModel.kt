package com.christianalexandre.fakestore.presentation.feature_screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christianalexandre.fakestore.data.local.SessionManager
import com.christianalexandre.fakestore.data.remote.backend.BackendApi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val api: BackendApi,
    private val sessionManager: SessionManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    sealed interface State {
        data object Loading : State
        data class Loaded(val name: String, val bio: String, val email: String?) : State
        data object Error : State
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val user = api.getUser(sessionManager.userId)
                _state.value = State.Loaded(name = user.name, bio = user.bio, email = user.email)
            } catch (e: Exception) {
                _state.value = State.Error
            }
        }
    }

    fun logout() {
        sessionManager.clear()
        firebaseAuth.signOut()
    }
}
