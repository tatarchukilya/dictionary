package ru.nblackie.dictionary.impl.domain.usecase.reed

import ru.nblackie.core.impl.data.Lang

/**
 * @author Ilya Tatarchuk
 */
internal interface RemoteCountUseCase {

    suspend fun run(lang: Lang = Lang.EN): Int
}