package ru.nblackie.dictionary.impl.domain.usecase.reed

import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.presentation.recycler.items.SearchItem
import ru.nblackie.dictionary.impl.domain.usecase.UseCase

/**
 * @author Ilya Tatarchuk
 */
internal interface RemoteSearchUseCase : UseCase {

    suspend fun run(input: String, lang: Lang = Lang.EN, limit: Int = 10) : List<SearchItem>
}