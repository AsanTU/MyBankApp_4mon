package com.example.mybankapp_4mon.di

import com.example.mybankapp_4mon.data.api.AccountApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL = "https://685a3a859f6ef96111557199.mockapi.io/"

    @Provides
    fun loginInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun httpClient(loginInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(loginInterceptor)
        .build()

    @Provides
    fun retrofit(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun accountApi(retrofit: Retrofit): AccountApi = retrofit.create(AccountApi::class.java)
}