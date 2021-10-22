package ru.nblackie.dictionary.impl.domain.model

import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.data.model.Translation

/**
 * @author tatarchukilya@gmail.com
 */
internal data class TranslationItem(val translation: Translation) : ListItem {

    override fun viewType(): Int = R.layout.view_translation
}