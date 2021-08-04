package ru.nblackie.remote.impl.di

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.nblackie.remote.REMOTE_TAG
import ru.nblackie.remote.getRetrofit
import ru.nblackie.remote.impl.dictionary.DictionaryApi
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal class RestModule {

    companion object {
        const val DICTIONARY_BASE_URL = "https://nblackie.ru"
    }

    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor {
        Log.d(REMOTE_TAG, it)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    @Named(DICTIONARY_BASE_URL)
    fun provideDictionaryRetrofit(httpClient: OkHttpClient): Retrofit =
        getRetrofit(httpClient, DICTIONARY_BASE_URL)

    @Provides
    @Singleton
    fun provideDictionaryService(@Named(DICTIONARY_BASE_URL) retrofit: Retrofit): DictionaryApi =
        retrofit.create(DictionaryApi::class.java)
}