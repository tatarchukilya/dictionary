package ru.nblackie.dictionary.impl.presentation.edit

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
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel

/**
 * @author Ilya Tatarchuk
 */
internal class EditBottomSheet : BottomSheetDialogFragment(), EditView {

    private lateinit var editText: CustomEditText
    private lateinit var saveView: ImageView

    private val viewModel: SharedViewModel by navGraphViewModels(R.id.graph_dictionary) {
        DictionaryFeatureHolder.getInternalApi().sharedViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editText = view.findViewById(R.id.edit_text)
        editText.run {
            onFocusChangeListener = View.OnFocusChangeListener { _, inFocus ->
                if (inFocus) {
                    dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                } else {
                    activity?.onBackPressed()
                }
            }
            doAfterTextChanged {
                edit(it.toString())
            }
        }
        saveView = view.findViewById(R.id.save_image_view)
        saveView.setOnClickListener {
            save()
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addTranslationState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                if (editText.text.toString() != it.translation) {
                    editText.setText(it.translation)
                }
                saveView.isVisible = it.wasChanged
            }
        }
    }

    override fun onResume() {
        super.onResume()
        editText.showKeyboard()
    }

    override fun edit(string: String) {
        //viewModel.editTranslation(string)
    }

    override fun save() {
        //viewModel.saveChanges()
    }
}