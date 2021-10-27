package ru.nblackie.dictionary.impl.di.internal

import dagger.MapKey
import ru.nblackie.dictionary.impl.domain.usecase.UseCase
import kotlin.reflect.KClass

/**
 * @author Ilya Tatarchuk
 */
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class UseCaseClassKey(val value: KClass<out UseCase>)
