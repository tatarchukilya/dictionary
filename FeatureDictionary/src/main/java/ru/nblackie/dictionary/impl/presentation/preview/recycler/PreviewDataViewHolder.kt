package ru.nblackie.dictionary.impl.presentation.preview.recycler

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ru.nblackie.core.recycler.BindViewHolder
import ru.nblackie.dictionary.R

/**
 * @author tatarchukilya@gmail.com
 */
internal class PreviewDataViewHolder(view: View) : BindViewHolder<PreviewDataItem>(view) {

    private val container = view.findViewById<LinearLayout>(R.id.container)
    //private val editImageView = view.findViewById<ImageView>(R.id.edit_content_image)
    private val actionClick: ((title: String, content: List<String>) -> Unit)? = null

    override fun onBind(item: PreviewDataItem) {
        container.removeAllViews()
        val title = LayoutInflater.from(itemView.context)
            .inflate(R.layout.view_title_secondary, container, false) as TextView
        title.text = item.title
        container.addView(title)
        item.content.forEach {
            val view = LayoutInflater.from(itemView.context)
                .inflate(R.layout.view_prymary_text, container, false) as TextView
            view.text = it
            container.addView(view)
        }
    }

    override fun unbind() {
        container.removeAllViews()
    }
}