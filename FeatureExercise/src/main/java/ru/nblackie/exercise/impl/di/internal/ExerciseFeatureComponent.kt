package ru.nblackie.exercise.impl.di.internal

import dagger.Component
import ru.nblackie.coredi.PerFeature
import ru.nblackie.exercise.impl.di.ExerciseFeatureDependency

/**
 * @author tatarchukilya@gmail.com
 */
@PerFeature
@Component(
    dependencies = [ExerciseFeatureDependency::class],
    modules = [ExerciseFeatureModule::class]
)
internal abstract class ExerciseFeatureComponent : ExerciseFeatureInternalApi {

    companion object {
        fun build(dependency: ExerciseFeatureDependency): ExerciseFeatureComponent =
            DaggerExerciseFeatureComponent.builder()
                .exerciseFeatureDependency(dependency)
                .build()!!
    }
}