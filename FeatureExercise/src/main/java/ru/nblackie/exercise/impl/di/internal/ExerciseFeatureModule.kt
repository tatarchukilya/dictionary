package ru.nblackie.exercise.impl.di.internal

import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
import ru.nblackie.core.fragment.ContainerFragment
import ru.nblackie.coredi.PerFeature
import ru.nblackie.exercise.R
import ru.nblackie.exercise.impl.presentation.ExerciseContainerFragment

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object ExerciseFeatureModule {

    @PerFeature
    @Provides
    fun provideExerciseContainerFragment(): ContainerFragment = ExerciseContainerFragment()

    @PerFeature
    @Provides
    fun provideNavHostFragment(): NavHostFragment = NavHostFragment.create(R.navigation.exersice)
}