package ru.nblackie.core.utils

/**
 * @author tatarchukilya@gmail.com
 */
fun String.firstCharUpperCase(): String = replaceFirstChar { it.titlecase() }
