package ru.nblackie.core.converter

/**
 * @author tatarchukilya@gmail.com
 */
interface Converter<T, R> {

    fun convert(from: T): R
}