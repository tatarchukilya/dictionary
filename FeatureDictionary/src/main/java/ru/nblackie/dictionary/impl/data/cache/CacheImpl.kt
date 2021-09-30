package ru.nblackie.corecache.impl.cache

import ru.nblackie.corecache.api.cache.Cache

/**
 * @author tatarchukilya@gmail.com
 */
class CacheImpl<K> : Cache<K> {

    private val store = hashMapOf<K, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <V> getIfExist(key: K, clazz: Class<V>): V? = store[key] as V?

    override fun isExist(key: K): Boolean {
        return store[key] != null
    }

    override fun <V> put(key: K, value: V) {
        store[key] = value!!
    }
}