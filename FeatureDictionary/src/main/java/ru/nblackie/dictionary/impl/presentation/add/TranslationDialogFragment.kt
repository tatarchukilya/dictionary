package ru.nblackie.dictionary.impl.presentation.add

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.nblackie.dictionary.R

/**
 * @author Ilya Tatarchuk
 */
class TranslationDialogFragment : DialogFragment() {

    private lateinit var editText: EditText

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = LayoutInflater.from(requireContext()).inflate(R.layout.view_edit_translation, null)
        editText = root.findViewById(R.id.edit_text)
        arguments?.getString(DATA_KE)?.let {
            editText.setText(it)
        }
        editText.onFocusChangeListener = OnFocusChangeListener { _, inFocus ->
            if (inFocus) {
                dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
        return AlertDialog.Builder(requireContext())
            .setView(root)
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ -> Toast.makeText(context, "positive", Toast.LENGTH_LONG).show() }
            .setNegativeButton(android.R.string.cancel, null)
            .setCancelable(true)
            .create()
    }

    override fun onResume() {
        super.onResume()
        editText.requestFocus()
    }

    companion object {
        private const val DATA_KE = "data_key"
        private const val INDEX_KEY = "index_key"

        fun newInstance(translation: String, index: Int): TranslationDialogFragment {
            return TranslationDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(DATA_KE, translation)
                    putInt(INDEX_KEY, index)
                }
            }
        }
    }
}