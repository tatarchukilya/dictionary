package ru.nblackie.exercise.impl.di

import android.util.Log
import ru.nblackie.coredi.DependencyFeatureHolder
import ru.nblackie.exercise.api.ExerciseFeatureApi
import ru.nblackie.exercise.impl.di.internal.ExerciseFeatureComponent

/**
 * @author tatarchukilya@gmail.com
 */
object ExerciseFeatureHolder : DependencyFeatureHolder<ExerciseFeatureApi, ExerciseFeatureDependency> {

    private var component: ExerciseFeatureComponent? = null

    override fun init(dependencies: ExerciseFeatureDependency) {
        Log.i("ExerciseFeatureHolder", "init()")
        if (component == null) {
            synchronized(ExerciseFeatureHolder::class.java) {
                if (component == null) {
                    Log.i("ExerciseFeatureComponent", "build")
                    component = ExerciseFeatureComponent.build(dependencies)
                }
            }
        }
    }

    override fun getApi(): ExerciseFeatureApi {
        checkNotNull(component) { "ExerciseFeatureComponent was not initialized!" }
        return component!!
    }

    override fun reset() {
        component = null
    }
}