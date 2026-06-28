package com.christianalexandre.fakestore.di

import android.util.Log
import com.christianalexandre.fakestore.common.Constants
import com.christianalexandre.fakestore.data.local.SessionManager
import com.christianalexandre.fakestore.data.remote.auth.AuthApi
import com.christianalexandre.fakestore.data.remote.backend.BackendApi
import com.christianalexandre.fakestore.data.remote.product.ProductsApi
import com.christianalexandre.fakestore.data.repository.AuthRepositoryImpl
import com.christianalexandre.fakestore.data.repository.OrderCartRepositoryImpl
import com.christianalexandre.fakestore.data.repository.ProductsRepositoryImpl
import com.christianalexandre.fakestore.domain.repository.AuthRepository
import com.christianalexandre.fakestore.domain.repository.CartRepository
import com.christianalexandre.fakestore.domain.repository.ProductsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Firebase
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // Network
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("HTTP_LOGGING", message)
                }
            }
        )
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    @Named("authenticated")
    fun provideAuthenticatedOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        sessionManager: SessionManager
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val token = sessionManager.token
                val request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }
            .build()

    @Provides
    @Singleton
    fun provideProductsApi(okHttpClient: OkHttpClient): ProductsApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(okHttpClient: OkHttpClient): AuthApi =
        Retrofit.Builder()
            .baseUrl(Constants.BACKEND_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideBackendApi(@Named("authenticated") okHttpClient: OkHttpClient): BackendApi =
        Retrofit.Builder()
            .baseUrl(Constants.BACKEND_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApi::class.java)

    // Repositories
    @Provides
    @Singleton
    fun provideProductsRepository(api: ProductsApi): ProductsRepository =
        ProductsRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideCartRepository(orderCartRepositoryImpl: OrderCartRepositoryImpl): CartRepository =
        orderCartRepositoryImpl

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository =
        authRepositoryImpl
}
