package com.christianalexandre.fakestore.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.christianalexandre.fakestore.common.Constants
import com.christianalexandre.fakestore.data.local.AppDatabase
import com.christianalexandre.fakestore.data.local.cart.dao.CartItemDao
import com.christianalexandre.fakestore.data.remote.product.ProductsApi
import com.christianalexandre.fakestore.data.repository.CartRepositoryImpl
import com.christianalexandre.fakestore.data.repository.ProductsRepositoryImpl
import com.christianalexandre.fakestore.domain.repository.CartRepository
import com.christianalexandre.fakestore.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // DB
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideCartDao(appDatabase: AppDatabase): CartItemDao {
        return appDatabase.cartDao()
    }

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
        // TODO(Remove when release)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideProductsApi(okHttpClient: OkHttpClient): ProductsApi =
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsApi::class.java)

    // Repositories
    @Provides
    @Singleton
    fun provideProductsRepository(api: ProductsApi): ProductsRepository =
        ProductsRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository {
        return cartRepositoryImpl
    }
}
