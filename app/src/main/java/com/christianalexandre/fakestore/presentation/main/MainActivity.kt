package com.christianalexandre.fakestore.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.christianalexandre.fakestore.data.local.SessionManager
import com.christianalexandre.fakestore.presentation.ui.theme.FakeStoreTheme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCM", "Token: ${task.result}")
            }
        }
        enableEdgeToEdge()
        setContent {
            FakeStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AppNavigation(isLoggedIn = sessionManager.token != null)
                }
            }
        }
    }
}
