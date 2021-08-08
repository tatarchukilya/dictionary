package ru.nblackie.remote.impl.dictionary.model

import ru.tatarchuk.personaldictionary.data.remote.rest.dictionary.model.Word

data class SearchResponse(
    val result: List<Word>,
    val status: Int
)