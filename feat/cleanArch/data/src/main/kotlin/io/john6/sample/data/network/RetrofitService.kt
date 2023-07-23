package io.john6.sample.data.network

import io.john6.sample.data.model.PostDTO
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://jsonplaceholder.typicode.com/"
val typiCodeRepo: RetrofitService by lazy {

    val client = OkHttpClient.Builder().addNetworkInterceptor {
        println(it.request().body()?.toString())
        it.proceed(it.request()).apply {
            println(this.networkResponse()?.body()?.toString())
            println(this.cacheResponse()?.body()?.toString())
        }
    }.build()

    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RetrofitService::class.java)
}

interface RetrofitService {
    @GET("/posts")
    suspend fun getPosts(): List<PostDTO>

    @GET("/posts/{id}")
    suspend fun getPost(@Path("id") id: String): PostDTO
}