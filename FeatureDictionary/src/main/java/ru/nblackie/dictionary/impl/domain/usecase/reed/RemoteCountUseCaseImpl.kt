package ru.nblackie.dictionary.impl.domain.usecase.reed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.domain.repository.RemoteRepository

/**
 * @author Ilya Tatarchuk
 */
internal class RemoteCountUseCaseImpl(private val repo: RemoteRepository) : RemoteCountUseCase {

    override suspend fun run(lang: Lang): Int {
        return withContext(Dispatchers.IO) {
            repo.count(lang.code)
        }
    }
}