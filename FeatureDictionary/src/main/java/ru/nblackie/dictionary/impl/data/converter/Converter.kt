package ru.nblackie.dictionary.impl.data.converter

import ru.nblackie.core.impl.data.Lang.EN
import ru.nblackie.core.impl.data.Lang.RU
import ru.nblackie.coredb.impl.db.data.WordFullData
import ru.nblackie.coredb.impl.db.data.WordLocal
import ru.nblackie.dictionary.impl.data.model.NewTranslation
import ru.nblackie.dictionary.impl.data.model.Word
import ru.nblackie.remote.impl.dictionary.model.search.RemoteSearchResult

/**
 * @author Ilya Tatarchuk
 */
internal fun NewTranslation.toFullData() =
    WordFullData(WordLocal(word, EN.code), transcription, listOf(WordLocal(translation, RU.code)))

internal fun RemoteSearchResult.toWord() = Word(word, transcription, translation)
