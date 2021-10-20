package ru.nblackie.dictionary.impl.di

import ru.nblackie.core.api.ResourceManager
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredi.FeatureDependency
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryDependency : FeatureDependency {

    fun dictionaryDao(): DictionaryDao

    fun dictionaryApi(): RemoteDictionaryApi

    fun resourceManager(): ResourceManager
}