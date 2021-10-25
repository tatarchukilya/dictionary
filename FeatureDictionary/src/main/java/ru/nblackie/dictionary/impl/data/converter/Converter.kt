package ru.nblackie.dictionary.impl.data.converter

import ru.nblackie.core.impl.data.Lang.EN
import ru.nblackie.core.impl.data.Lang.RU
import ru.nblackie.coredb.impl.db.data.FullSearchRow
import ru.nblackie.coredb.impl.db.data.TranslationSearchRow
import ru.nblackie.coredb.impl.db.data.WordFullData
import ru.nblackie.coredb.impl.db.data.WordLocal
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.data.model.Translation
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.remote.impl.dictionary.model.search.SearchResultRest

/**
 * @author Ilya Tatarchuk
 */
internal fun NewTranslation.toFullData() =
    WordFullData(WordLocal(word, EN.code), transcription, listOf(WordLocal(translation, RU.code)))

/**
 * Конвертирует дынные из БД, сгруппированые в [Map] методом [List.groupBy] по слову, в объект [SearchResult]
 */
internal fun Map.Entry<String, List<FullSearchRow>>.toSearchResult(): SearchResult {
    return SearchResult(key, value[0].transcription, value.map { Translation(it.translation, true) })
}

/**
 * Совмещает данные загруженые с сервера [SearchResultRest] с данными из БД.
 * Если вариант перевода есть в БД, то [Translation.isAdded] = true, иначе false
 *
 * @param local варианты перевода в БД
 */
internal fun SearchResultRest.toSearchResult(local: List<TranslationSearchRow>?): SearchResult {
    return SearchResult(word, transcription, translation.map {
        Translation(it, local?.find { row -> row.translation == it } != null)
    }.sortedBy { !it.isAdded })
}
