package ru.nblackie.dictionary.impl.data.repository

import ru.nblackie.coredb.impl.db.data.TranslationSearchRow
import ru.nblackie.remote.impl.dictionary.model.SearchResultRest

/**
 * @author Ilya Tatarchuk
 */
typealias Translations = Map<String, List<TranslationSearchRow>>

typealias RemoteResult = List<SearchResultRest>