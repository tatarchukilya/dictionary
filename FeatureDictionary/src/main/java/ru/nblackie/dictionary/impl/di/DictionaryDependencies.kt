package ru.nblackie.dictionary.impl.di

import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredi.FeatureDependencies
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryDependencies : FeatureDependencies {

    fun dictionaryDao(): DictionaryDao

    fun dictionaryApi(): RemoteDictionaryApi

}