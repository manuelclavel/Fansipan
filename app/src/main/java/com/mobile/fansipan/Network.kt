package com.mobile.fansipan

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST



interface LambdaApiService {
    //@POST("")
    suspend fun invokeLambda(@Body requestBody: ReadingRecord): String
}

// 1. Create a single OkHttpClient instance
val sharedOkHttpClient =
    OkHttpClient.Builder() // Add any desired interceptors, timeouts, etc. to the shared client
        //.addInterceptor())
        .build()



// 2. Create the first Retrofit instance, using the shared OkHttpClient
var retrofit1 = Retrofit.Builder()
    .baseUrl("https://api.example.com/v1/")
    .client(sharedOkHttpClient) // Pass the shared client
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// 3. Create the second Retrofit instance, also using the shared OkHttpClient
var retrofit2 = Retrofit.Builder()
    .baseUrl("https://api.another-example.com/v2/")
    .client(sharedOkHttpClient) // Pass the shared client again
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val service1 = retrofit1.create(LambdaApiService::class.java)
val service2 = retrofit1.create(LambdaApiService::class.java)
