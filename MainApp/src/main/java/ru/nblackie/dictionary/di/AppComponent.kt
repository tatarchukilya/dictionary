package ru.nblackie.dictionary.di

import dagger.Component
import dagger.internal.Preconditions
import ru.nblackie.dictionary.DictionaryApplication

/**
 * @author tatarchukilya@gmail.com
 */
@Component(modules = [AppModule::class])
abstract class AppComponent {

    abstract fun inject(daggerArchApplication: DictionaryApplication)

    companion object {

        @Volatile
        private var instance: AppComponent? = null

        fun get(): AppComponent {
            return Preconditions.checkNotNull(
                instance,
                "AppComponent is not initialized yet. Call init first."
            )!!
        }

        fun init(component: AppComponent) {
            require(instance == null) { "AppComponent is already initialized." }
            instance = component
        }
    }
}