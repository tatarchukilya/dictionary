package ru.nblackie.remote.impl.di.internal

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.nblackie.remote.REMOTE_TAG
import ru.nblackie.remote.getRetrofit
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object RestModule {

    private const val DICTIONARY_BASE_URL = "https://nblackie.ru"

    @JvmStatic
    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor {
        Log.d(REMOTE_TAG, it)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @JvmStatic
    @Provides
    @Singleton
    @Named(DICTIONARY_BASE_URL)
    fun provideDictionaryRetrofit(httpClient: OkHttpClient): Retrofit =
        getRetrofit(httpClient, DICTIONARY_BASE_URL)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDictionaryService(@Named(DICTIONARY_BASE_URL) retrofit: Retrofit): RemoteDictionaryApi =
        retrofit.create(RemoteDictionaryApi::class.java)
}