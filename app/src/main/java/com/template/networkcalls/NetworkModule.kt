package com.template.networkcalls

import androidx.databinding.library.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.template.utils.hideProgress
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun cacheUtil() = CacheUtil()

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    fun tokenAuthenticator() = TokenAuthenticator()

    @Singleton
    @Provides
    fun provideOkHttpClient(tokenAuthenticator: TokenAuthenticator): OkHttpClient =
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                /*  .addInterceptor { chain ->
                      val original = chain.request()
                      val request = original.newBuilder()
                          .header("Authorization", "token")
                          .header("lang", "ENGLISH")
                          //                    .method(original.method, original.body)
                          .build()
                      chain.proceed(request)
                  }*/
                .addInterceptor(loggingInterceptor)
                .authenticator(tokenAuthenticator)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .build()
        }

    @Singleton
    @Provides
    fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        BASE_URL: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): RetrofitApi =
        retrofit.create(RetrofitApi::class.java)

    @Provides
    @Singleton
    fun exceptionHandler() = CoroutineExceptionHandler { _, t ->
        t.printStackTrace()

        CoroutineScope(Dispatchers.Main).launch {
            hideProgress()
            t.printStackTrace()
        }
    }
}