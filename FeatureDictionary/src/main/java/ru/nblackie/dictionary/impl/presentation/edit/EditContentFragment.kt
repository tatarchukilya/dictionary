package ru.nblackie.dictionary.impl.presentation.edit

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import ru.nblackie.core.impl.utils.firstCharUpperCase
import ru.nblackie.core.impl.utils.showKeyboard
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment

/**
 * @author tatarchukilya@gmail.com
 */
internal class EditContentFragment : ViewModelFragment(R.layout.fragment_edit),
    Transition.TransitionListener {

    private lateinit var toolbar: Toolbar
    private lateinit var editTextView: EditText
    private lateinit var saveImage: ImageView
    private lateinit var rollbackImage: ImageView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context)
                .inflateTransition(android.R.transition.move).apply {
                    addListener(this@EditContentFragment)
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar(view)
        setUpView(view)
        setUpObserver()
    }

    private fun setUpView(view: View) {
        editTextView = view.findViewById(R.id.edit_text)
        arguments?.getInt(POSITION_KEY)?.let {
            editTextView.transitionName = getTransitionName(it)
        }

        editTextView.doAfterTextChanged {
            it.toString().let { str ->
                viewModel.setEditedTranslation(str)
            }
        }
        saveButton = view.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            saveAndFinish()
        }
        saveImage = view.findViewById(R.id.save_image_view)
        saveImage.setOnClickListener {
            editTextView.setSelection(editTextView.text.length / 2)
        }
        rollbackImage = view.findViewById(R.id.rollback_image_view)
        rollbackImage.setOnClickListener {
            viewModel.rollback()
        }
    }

    private fun setUpToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setUpObserver() {
        viewModel.selectedItem.observe(viewLifecycleOwner, {
            toolbar.title = it.word.firstCharUpperCase()
        })
        viewModel.editTranslation.observe(viewLifecycleOwner, {
            if (editTextView.text.toString() != it) {
                editTextView.setText(it)
            }
        })
    }

    private fun saveAndFinish() {
        viewModel.saveTranslation()
        activity?.onBackPressed()
    }

    private fun View.setAnimationVisibility(isVisible: Boolean) {
        if (this.isVisible == isVisible) return
        val animationX = ObjectAnimator.ofFloat(this, "scaleX", if (isVisible) 1f else 0f)
        val animationY = ObjectAnimator.ofFloat(this, "scaleY", if (isVisible) 1f else 0f)
        AnimatorSet().apply {
            play(animationX).with(animationY)
            duration = 200
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    if (isVisible) {
                        visibility = View.VISIBLE
                    }
                }

                override fun onAnimationEnd(p0: Animator?) {
                    if (!isVisible) {
                        visibility = View.GONE
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationRepeat(p0: Animator?) {}

            })
        }.start()
    }

    // TransitionListener
    override fun onTransitionStart(transition: Transition) {
        saveButton.isVisible = false
    }

    override fun onTransitionEnd(transition: Transition) {
        // Без Handler editTextView.requestFocus работает некорректно
        Handler(Looper.getMainLooper()).post {
            saveButton.isVisible = true
            editTextView.showKeyboard()
        }
    }

    override fun onTransitionCancel(transition: Transition) {}
    override fun onTransitionPause(transition: Transition) {}
    override fun onTransitionResume(transition: Transition) {}

    companion object {
        private const val POSITION_KEY = "position_key"

        fun createArgs(position: Int = -1): Bundle = Bundle().apply {
            putInt(POSITION_KEY, position)
        }

        fun getTransitionName(position: Int) = "EditContentFragment$position"
    }
}