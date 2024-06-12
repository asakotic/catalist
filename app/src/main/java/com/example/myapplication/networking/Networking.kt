package com.example.myapplication.networking

import com.example.myapplication.networking.serialization.AppJson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor{
        val updatedRequest = it.request().newBuilder()
            .addHeader("x-api-key","live_F0vzWugKscHoAB3PRBDWXwaTxJil8BUows424ZppT8WkdumOzWsHLnhcOEvLLRHy")
            .build()
        it.proceed(updatedRequest)
    }
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    )
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/")
    .client(okHttpClient)
    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
    .build()