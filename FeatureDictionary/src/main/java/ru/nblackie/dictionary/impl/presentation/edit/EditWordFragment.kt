package ru.nblackie.dictionary.impl.presentation.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ru.nblackie.dictionary.R
import ru.nblackie.remote.impl.dictionary.model.Word
import kotlin.math.log

/**
 * @author tatarchukilya@gmail.com
 */
class EditWordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO add share transition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit_word, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar(view)
    }

    private fun setUpToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar).apply {
            navigationIcon =
                AppCompatResources.getDrawable(context, R.drawable.ic_arrow_back_24)
        }
        toolbar.title = arguments?.getString(WORD_KEY)?.replaceFirstChar {
            it.titlecase()
        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {

        private const val WORD_KEY = "word_arg"
        private const val TRANSCRIPTION_KEY = "transcription_key"
        private const val TRANSLATION_KEY = "translation_key"

        fun createArgs(word: String, transcription: String, translation: ArrayList<String>) =
            Bundle().apply {
                putString(WORD_KEY, word)
                putString(TRANSCRIPTION_KEY, transcription)
                putStringArrayList(TRANSLATION_KEY, translation)
            }

        fun createArgs(word: String, transcription: String, translation: String): Bundle {
            return createArgs(word, transcription, ArrayList(translation.split(",")))
        }
    }
}