package ru.nblackie.dictionary.impl.domain.usecase.reed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.domain.converter.toDictionaryItem
import ru.nblackie.dictionary.impl.domain.repository.DbRepository
import ru.nblackie.dictionary.impl.presentation.recycler.items.DictionaryItem

/**
 * @author Ilya Tatarchuk
 */
internal class DictionaryUseCaseImpl(private val repository: DbRepository) : DictionaryUseCase {

    override suspend fun run(lang: Lang): List<DictionaryItem> {
        return withContext(Dispatchers.IO) {
            repository.getDictionary(lang.code).map { it.toDictionaryItem() }
        }
    }
}