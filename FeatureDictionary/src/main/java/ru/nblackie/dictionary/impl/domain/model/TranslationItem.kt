package ru.nblackie.dictionary.impl.domain.model

import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.R

/**
 * @author tatarchukilya@gmail.com
 */
data class TranslationItem(val translation: String, val isAdded: Boolean) : ListItem {

    override fun viewType(): Int = R.layout.view_translation
}