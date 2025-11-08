package com.christianalexandre.fakestore.presentation.feature_screens.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

// TODO: This screen was did using firebase-ui library and should be replaced with a custom implementation.
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val signInLauncher =
        rememberLauncherForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("LoginScreen", "Login success")
                onLoginSuccess()
            } else {
                val response = result.idpResponse
                if (response == null) {
                    Log.w("LoginScreen", "Login cancelled")
                } else {
                    Log.e("LoginScreen", "Login error: ${response.error?.errorCode}")
                }
            }
        }

    val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

    val signInIntent =
        AuthUI
            .getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setCredentialManagerEnabled(false)
            .build()

    LaunchedEffect(Unit) {
        signInLauncher.launch(signInIntent)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
