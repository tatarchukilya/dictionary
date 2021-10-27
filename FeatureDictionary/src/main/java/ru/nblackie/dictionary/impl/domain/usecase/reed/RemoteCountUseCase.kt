package ru.nblackie.dictionary.impl.domain.usecase.reed

import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.domain.usecase.UseCase

/**
 * @author Ilya Tatarchuk
 */
internal interface RemoteCountUseCase : UseCase {

    suspend fun run(lang: Lang = Lang.EN): Int
}