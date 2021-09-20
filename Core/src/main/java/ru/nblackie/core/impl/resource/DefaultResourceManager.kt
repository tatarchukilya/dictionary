package ru.nblackie.core.impl.resource

import android.content.Context
import androidx.annotation.StringRes
import ru.nblackie.core.api.ResourceManager

/**
 * @author tatarchukilya@gmail.com
 */
internal class DefaultResourceManager(context: Context) : ResourceManager {

    private val resources = context.resources

    override fun getString(@StringRes resId: Int): String {
        return resources.getString(resId)
    }
}