package com.test.fairmoney.di

import android.content.Context
import androidx.room.Room
import com.test.fairmoney.App
import com.test.fairmoney.BuildConfig
import com.test.fairmoney.model.NetworkState
import com.test.fairmoney.model.local.AppDatabase
import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.remote.ApiService
import com.test.fairmoney.utils.NetworkStateImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class AppModule {

    private val connectTimeout = 10L
    private val readTimeout = 10L
    private val writeTimeout = 10L

    @Provides
    @Singleton
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    @Singleton
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

    @Provides
    @Singleton
    fun providesAppDatabase(context: Context) : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "spotter_base_db").fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesAppDao(database: AppDatabase): AppDao {
        return database.appDao()
    }

    @Provides
    @Singleton
    fun providesNetworkState(ctx: Context): NetworkState {
        return NetworkStateImpl(ctx)
    }
}