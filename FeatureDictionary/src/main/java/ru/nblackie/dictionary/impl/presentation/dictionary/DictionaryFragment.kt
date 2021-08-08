package ru.nblackie.dictionary.impl.presentation.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import androidx.navigation.NavOptions




/**
 * @author tatarchukilya@gmail.com
 */
internal class DictionaryFragment : Fragment() {

    private lateinit var viewModel: DictionaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, DictionaryFeatureHolder
                .getInternalApi()
                .dictionaryViewModelProviderFactory()
        ).get(DictionaryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dictionary, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            findViewById<Button>(R.id.next).setOnClickListener {
//                val options = NavOptions.Builder()
//                    .setLaunchSingleTop(true)
//                    .build()
                findNavController().navigate(R.id.fragment_search)//, null, options)
            }
            findViewById<Button>(R.id.back).setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}