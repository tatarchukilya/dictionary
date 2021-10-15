package ru.nblackie.dictionary.impl.data.converter

import ru.nblackie.coredb.impl.db.data.SearchRow
import ru.nblackie.coredb.impl.db.data.WordData
import ru.nblackie.coredb.impl.db.data.WordLocal
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.NewTranslation

/**
 * @author Ilya Tatarchuk
 */
fun NewTranslation.toDataSaveEnRu() =
    WordData(WordLocal(word, "en"), transcription, listOf(WordLocal(translation, "ru")))

fun List<SearchRow>.toListResult(): List<SearchResult> {
    val result: MutableList<SearchResult> = mutableListOf()
    groupBy { it.word }.forEach {
        result.add(it.toSearchResult())
    }
    return result
}

fun Map.Entry<String, List<SearchRow>>.toSearchResult(): SearchResult {
    return SearchResult(key, value[0].transcription, mutableListOf<String>().apply {
        value.forEach {
            add(it.translation)
        }
    })
}