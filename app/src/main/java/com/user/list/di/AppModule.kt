package com.user.list.di

import android.content.Context
import androidx.room.Room
import com.user.list.App
import com.user.list.BuildConfig
import com.user.list.model.NetworkState
import com.user.list.model.local.AppDatabase
import com.user.list.model.local.dao.AppDao
import com.user.list.model.remote.ApiService
import com.user.list.utils.NetworkStateImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class AppModule {

    private val connectTimeout = 10L
    private val readTimeout = 10L
    private val writeTimeout = 10L

    @[Provides Singleton]
    fun providesApiService(): ApiService {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val clientBuilder = OkHttpClient.Builder().addInterceptor(
            if (BuildConfig.DEBUG) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpLoggingInterceptor
            } else {
                httpLoggingInterceptor
            }
        ).connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder().header("app-id", BuildConfig.APP_ID).build()
                return@addInterceptor chain.proceed(request)
            }

        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder
            .client(clientBuilder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @[Provides Singleton]
    fun providesAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "user_list_db").fallbackToDestructiveMigration().build()
    }

    @[Provides Singleton]
    fun providesAppDao(database: AppDatabase): AppDao {
        return database.appDao()
    }

    @[Provides Singleton]
    fun providesNetworkState(@ApplicationContext ctx: Context): NetworkState {
        return NetworkStateImpl(ctx)
    }
}
