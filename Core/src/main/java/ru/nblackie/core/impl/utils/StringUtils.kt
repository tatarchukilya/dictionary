package ru.nblackie.core.impl.utils

/**
 * @author tatarchukilya@gmail.com
 */
fun String.firstCharUpperCase(): String = replaceFirstChar { it.titlecase() }
