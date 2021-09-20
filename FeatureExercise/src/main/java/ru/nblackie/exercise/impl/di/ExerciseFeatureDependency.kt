package ru.nblackie.exercise.impl.di

import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredi.FeatureDependency

/**
 * @author tatarchukilya@gmail.com
 */
interface ExerciseFeatureDependency : FeatureDependency{

    fun dao() : DictionaryDao
}