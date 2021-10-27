package ru.nblackie.dictionary.impl.domain.usecase.reed

import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.domain.usecase.UseCase

/**
 * @author Ilya Tatarchuk
 */
internal interface DbSearchUseCase : UseCase {

    suspend fun run(input: String, lang: Lang = Lang.EN): List<SearchItem>
}