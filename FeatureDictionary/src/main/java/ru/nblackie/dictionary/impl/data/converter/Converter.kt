package ru.nblackie.dictionary.impl.data.converter

import android.util.Log
import ru.nblackie.core.impl.data.Lang.EN
import ru.nblackie.core.impl.data.Lang.RU
import ru.nblackie.coredb.impl.db.data.FullSearchRow
import ru.nblackie.coredb.impl.db.data.TranslationSearchRow
import ru.nblackie.coredb.impl.db.data.WordFullData
import ru.nblackie.coredb.impl.db.data.WordLocal
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.data.model.Translation
import ru.nblackie.dictionary.impl.data.repository.RemoteResult
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.remote.impl.dictionary.model.SearchResultRest

/**
 * @author Ilya Tatarchuk
 */
internal fun NewTranslation.toFullData() =
    WordFullData(WordLocal(word, EN.code), transcription, listOf(WordLocal(translation, RU.code)))

internal fun List<FullSearchRow>.toListResult(): List<SearchResult> {
    val result: MutableList<SearchResult> = mutableListOf()
    groupBy { it.word }.forEach {
        result.add(it.toSearchResult())
    }
    return result
}

internal fun Map.Entry<String, List<FullSearchRow>>.toSearchResult(): SearchResult {
    return SearchResult(key, value[0].transcription, mutableListOf<Translation>().apply {
        value.forEach {
            add(Translation(it.translation))
        }
    })
}

internal fun SearchResultRest.toSearchResult(local: List<TranslationSearchRow>?): SearchResult {
    val tr = mutableListOf<Translation>().apply {
        translation.forEach {
            add(Translation(it, local?.find { row -> row.translation == it } != null))
        }
    }
    tr.sortBy { !it.isAdded }
    return SearchResult(word, transcription, tr)
}

internal fun RemoteResult.getWords(): List<String> {
    val result = mutableListOf<String>()
    forEach {
        result.add(it.word)
    }
    return result
}