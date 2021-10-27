package ru.nblackie.dictionary.impl.domain.usecase.reed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.data.cache.Cache
import ru.nblackie.dictionary.impl.data.model.Word
import ru.nblackie.dictionary.impl.domain.converter.concat
import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.domain.repository.DbRepository
import ru.nblackie.dictionary.impl.domain.repository.RemoteRepository

/**
 * @author Ilya Tatarchuk
 */
internal class RemoteSearchUseCaseImpl(
    private val remote: RemoteRepository,
    private val db: DbRepository,
    private val cache: Cache<String>,
    private val resourceManager: ResourceManager
) : RemoteSearchUseCase {

    override suspend fun run(input: String, lang: Lang, limit: Int): List<SearchItem> {
        return withContext(Dispatchers.IO) {
            val dbResult = db.search(input, lang.code)
            searchRemote(input, lang, limit)
                .map { word -> word.concat(dbResult.find { it.data == word.data }, resourceManager) }
        }
    }

    /**
     * Если по ключу [input] в кэше ничего не нашлось, загружает данные с сервера
     */
    private suspend fun searchRemote(input: String, lang: Lang, limit: Int): List<Word> {
        return cache.get(input, List::class.java) { str -> remote.search(str, lang.code, limit) } as List<Word>
    }
}