package ru.nblackie.remote.impl.di

import dagger.Component
import ru.nblackie.remote.api.RestApi
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Component(modules = [RestModule::class])
@Singleton
abstract class RestComponent : RestApi {

    companion object {
        @Volatile
        private var instance: RestComponent? = null

        fun get(): RestComponent {
            if (instance == null) {
                synchronized(RestComponent::class.java) {
                    if (instance == null) {
                        instance = DaggerRestComponent.builder().build()
                    }
                }
            }
            return instance!!
        }
    }
}