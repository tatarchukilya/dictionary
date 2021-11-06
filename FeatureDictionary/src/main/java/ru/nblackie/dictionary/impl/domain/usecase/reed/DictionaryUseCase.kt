package ru.nblackie.dictionary.impl.domain.usecase.reed

import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.domain.usecase.UseCase
import ru.nblackie.dictionary.impl.presentation.recycler.items.DictionaryItem

/**
 * @author Ilya Tatarchuk
 */
internal interface DictionaryUseCase : UseCase {

    suspend fun run(lang: Lang = Lang.EN) : List<DictionaryItem>
}