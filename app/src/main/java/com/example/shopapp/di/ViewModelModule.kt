package com.example.shopapp.di

import com.example.shopapp.data.local.DataStoreManager
import com.example.shopapp.data.network.ApiService
import com.example.shopapp.qualifiers.Token
import com.example.shopapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideApiService(okHttpClient: OkHttpClient): ApiService = Retrofit.Builder().run {
        baseUrl(Constants.BASE_URL)
        client(okHttpClient)
        addConverterFactory(GsonConverterFactory.create())
        build()
    }.create(ApiService::class.java)

    @Provides
    @ViewModelScoped
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    @ViewModelScoped
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        @Token token: String = ""
    ): OkHttpClient =
        OkHttpClient.Builder().addInterceptor { interceptor ->
            val request: Request = interceptor.request()
            val newReq = if (token.isNotEmpty()) {
                request.newBuilder()
                    .header("lang", "en")
                    .header("Content-Type", "application/json")
                    .header("Authorization",token)
                    .method(request.method, request.body)
                    .build()
            } else {
                request.newBuilder()
                    .header("lang", "en")
                    .header("Content-Type", "application/json")
                    .method(request.method, request.body)
                    .build()
            }
            interceptor.proceed(newReq)
        }
            .addNetworkInterceptor(interceptor)
            .build()

    @Provides
    @ViewModelScoped
    @Token
     fun provideTokenUser(dataStoreManager: DataStoreManager): String = dataStoreManager.userToken.value?.token.toString()

}