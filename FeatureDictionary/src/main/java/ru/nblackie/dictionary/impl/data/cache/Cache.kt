package ru.nblackie.dictionary.impl.data.cache

/**
 * @author tatarchukilya@gmail.com
 */
interface Cache<K> {

    fun <V> getIfExist(key: K, clazz: Class<V>): V?

    fun isExist(key: K): Boolean

    fun <V> put(key: K, value: V)

    suspend fun <V> get(key: K, clazz: Class<V>, getDataFun: suspend (K) -> V): V {
        return getIfExist(key, clazz) ?: getDataFun(key).also { put(key, it) }
    }
}