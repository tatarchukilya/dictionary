package ru.nblackie.settings.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ru.nblackie.settings.R

/**
 * @author tatarchukilya@gmail.com
 */
class SettingsRootFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings_root, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            findViewById<Button>(R.id.next).setOnClickListener {
                findNavController().navigate(R.id.fragment_settings)
            }
        }
    }
}