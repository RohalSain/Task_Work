package com.example.rohal.task_work

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class RetrofitClient22
{
    val BASE_URL = "http://104.236.194.108:4010"
    private var retrofit: Retrofit? = null


    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return this.retrofit
    }
}