package ru.nblackie.dictionary.impl.domain.usecase.reed

import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.data.model.Word
import ru.nblackie.dictionary.impl.domain.model.SearchItem

/**
 * @author Ilya Tatarchuk
 */
internal interface DbSearchUseCase {

    suspend fun run(input: String, lang: Lang = Lang.EN): List<SearchItem>
}