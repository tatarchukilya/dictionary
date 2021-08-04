package ru.nblackie.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author tatarchukilya@gmail.com
 */

const val REMOTE_TAG = "Dictionary<>"

fun getRetrofit(httpClient: OkHttpClient, baseUrl: String) = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(httpClient)
    .baseUrl(baseUrl)
    .build()
