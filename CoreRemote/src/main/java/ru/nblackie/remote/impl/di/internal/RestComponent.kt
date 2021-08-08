package ru.nblackie.remote.impl.di.internal

import dagger.Component
import ru.nblackie.remote.api.RestApi
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Component(modules = [RestModule::class])
@Singleton
internal interface RestComponent : RestApi {

    companion object {
        fun build() = DaggerRestComponent.builder()
            .build()!!
    }
}