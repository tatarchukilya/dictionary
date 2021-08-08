package ru.nblackie.coredb.api

import ru.nblackie.coredi.FeatureApi
import ru.nblackie.coredb.impl.db.DictionaryDao

/**
 * @author tatarchukilya@gmail.com
 */
interface DataBaseApi : FeatureApi {

    fun dao(): DictionaryDao
}