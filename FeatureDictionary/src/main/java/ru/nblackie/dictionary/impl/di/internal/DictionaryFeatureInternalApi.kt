package ru.nblackie.dictionary.impl.di.internal

import ru.nblackie.dictionary.api.di.DictionaryFeatureApi
import ru.nblackie.dictionary.impl.domain.usecase.UseCase

/**
 *  Предоставляет internal зависимости модуля
 *
 * @author tatarchukilya@gmail.com
 */
internal interface DictionaryFeatureInternalApi : DictionaryFeatureApi {

    fun useCases(): Map<Class<out UseCase>, UseCase>
}