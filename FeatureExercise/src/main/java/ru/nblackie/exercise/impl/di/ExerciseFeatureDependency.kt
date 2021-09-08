package ru.nblackie.exercise.impl.di

import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredi.FeatureDependencies

/**
 * @author tatarchukilya@gmail.com
 */
interface ExerciseFeatureDependency : FeatureDependencies{

    fun dao() : DictionaryDao
}