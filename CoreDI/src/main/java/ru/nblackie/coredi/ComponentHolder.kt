package ru.nblackie.coredi

/**
 * @author tatarchukilya@gmail.com
 */
interface ComponentHolder<C : BaseApi, D : BaseDependencies> {

    fun init(dependencies: D)

    fun get(): C

    fun reset()
}

interface BaseDependencies

interface BaseApi