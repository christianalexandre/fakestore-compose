package com.christianalexandre.fakestore.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("fakestore_session", Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString("token", null)
        set(value) { prefs.edit().putString("token", value).apply() }

    var userId: Long
        get() = prefs.getLong("userId", -1L)
        set(value) { prefs.edit().putLong("userId", value).apply() }

    fun clear() = prefs.edit().clear().apply()
}
