package ru.nblackie.example.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ru.nblackie.example.R

/**
 * @author tatarchukilya@gmail.com
 */
class ExerciseRootFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_exercise_root, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            findViewById<Button>(R.id.next).setOnClickListener {
                findNavController().navigate(R.id.fragment_exercise)
            }
            findViewById<Button>(R.id.back).setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}