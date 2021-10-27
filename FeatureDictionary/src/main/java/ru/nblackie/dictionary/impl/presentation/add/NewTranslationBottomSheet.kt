package ru.nblackie.dictionary.impl.presentation.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nblackie.core.impl.utils.showKeyboard
import ru.nblackie.core.impl.viewmodel.ViewModelProviderFactory
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import ru.nblackie.dictionary.impl.presentation.core.NewTranslation
import ru.nblackie.dictionary.impl.presentation.core.SaveNewTranslation
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel
import ru.nblackie.dictionary.impl.presentation.core.StopSelf

/**
 * @author Ilya Tatarchuk
 */
internal class NewTranslationBottomSheet : BottomSheetDialogFragment(), NewTranslationView {

    private lateinit var editText: CustomEditText
    private lateinit var saveView: ImageView

    private val viewModel: SharedViewModel by navGraphViewModels(R.id.graph_dictionary) {
        ViewModelProviderFactory({ SharedViewModel(DictionaryFeatureHolder.getInternalApi().useCases()) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpView(view)
        setUpObserver()
    }

    override fun onResume() {
        super.onResume()
        editText.showKeyboard()
    }

    override fun setNewTranslation(input: String) {
        viewModel.handleAction(NewTranslation(input))
    }

    override fun saveNewTranslation() {
        viewModel.handleAction(SaveNewTranslation)
    }

    override fun stopSelf() {
        findNavController().popBackStack()
    }

    private fun setUpView(view: View) {
        editText = view.findViewById(R.id.edit_text)
        editText.run {
            onFocusChangeListener = View.OnFocusChangeListener { _, inFocus ->
                if (inFocus) {
                    dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                } else {
                    stopSelf()
                }
            }
            doAfterTextChanged {
                setNewTranslation(it.toString())
            }
        }
        saveView = view.findViewById(R.id.save_image_view)
        saveView.setOnClickListener {
            saveNewTranslation()
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newTranslationState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                if (editText.text.toString() != it.translation) {
                    editText.setText(it.translation)
                }
                saveView.isVisible = it.wasChanged
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newTranslationEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                if (it is StopSelf) {
                    stopSelf()
                }
            }
        }
    }
}