package ru.nblackie.exercise.api

import androidx.navigation.fragment.NavHostFragment
import ru.nblackie.core.fragment.ContainerFragment
import ru.nblackie.coredi.FeatureApi

/**
 * @author tatarchukilya@gmail.com
 */
interface ExerciseFeatureApi : FeatureApi {

    fun navHostFragment(): NavHostFragment

    fun containerFragment(): ContainerFragment
}