package ru.nblackie.dictionary.impl.domain.usecase.reed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.domain.converter.toSearchItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.SearchItem
import ru.nblackie.dictionary.impl.domain.repository.DbRepository

/**
 * @author Ilya Tatarchuk
 */
internal class DbSearchUseCaseImpl(private val repository: DbRepository) : DbSearchUseCase {

    override suspend fun run(input: String, lang: Lang): List<SearchItem> {
        return withContext(Dispatchers.IO) {
            repository.search(input, lang.code).map { it.toSearchItem() }
        }
    }
}