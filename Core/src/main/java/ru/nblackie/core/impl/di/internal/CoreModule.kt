package ru.nblackie.core.impl.di.internal

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.core.impl.resource.DefaultResourceManager
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object CoreModule {

    @Provides
    @Singleton
    fun provideResourceManager(context: Context): ResourceManager = DefaultResourceManager(context)
}