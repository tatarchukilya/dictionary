package ru.nblackie.dictionary.impl.data.converter

import ru.nblackie.core.impl.data.Lang.EN
import ru.nblackie.core.impl.data.Lang.RU
import ru.nblackie.coredb.impl.db.data.FullSearchRow
import ru.nblackie.coredb.impl.db.data.WordFullData
import ru.nblackie.coredb.impl.db.data.WordLocal
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.data.repository.RemoteResult
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.remote.impl.dictionary.model.SearchResultRest

/**
 * @author Ilya Tatarchuk
 */
fun NewTranslation.toFullData() =
    WordFullData(WordLocal(word, EN.code), transcription, listOf(WordLocal(translation, RU.code)))

fun List<FullSearchRow>.toListResult(): List<SearchResult> {
    val result: MutableList<SearchResult> = mutableListOf()
    groupBy { it.word }.forEach {
        result.add(it.toSearchResult())
    }
    return result
}

fun Map.Entry<String, List<FullSearchRow>>.toSearchResult(): SearchResult {
    return SearchResult(key, value[0].transcription, mutableListOf<String>().apply {
        value.forEach {
            add(it.translation)
        }
    }, listOf())
}

fun SearchResultRest.toSearchResult(localTranslation: List<String>): SearchResult {
    return SearchResult(word, transcription, translation, localTranslation)
}

fun RemoteResult.getWords(): List<String> {
    val result = mutableListOf<String>()
    forEach {
        result.add(it.word)
    }
    return result
}