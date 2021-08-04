package ru.nblackie.dictionary

import android.app.Application
import android.content.Context
import ru.nblackie.dictionary.di.AppComponent
import ru.nblackie.dictionary.di.AppModule
import ru.nblackie.dictionary.di.DaggerAppComponent

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        AppComponent.init(
            DaggerAppComponent
                .builder()
                .build()
        )
        AppComponent.get().inject(this)
    }

    companion object {
        @Volatile
        lateinit var appContext: Context
            private set
    }
}