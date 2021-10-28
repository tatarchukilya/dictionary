package ru.nblackie.dictionary.impl.domain.usecase.reed

import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.domain.usecase.UseCase

/**
 * @author Ilya Tatarchuk
 */
internal interface DictionaryUseCase : UseCase {

    suspend fun run(lang: Lang = Lang.EN) : List<SearchItem>
}