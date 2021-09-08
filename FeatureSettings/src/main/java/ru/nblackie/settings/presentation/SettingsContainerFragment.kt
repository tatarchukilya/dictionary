package ru.nblackie.settings.presentation

import androidx.fragment.app.Fragment
import ru.nblackie.core.fragment.ContainerFragment

/**
 * @author tatarchukilya@gmail.com
 */
internal class SettingsContainerFragment : ContainerFragment() {

    override fun rootFragment(): Fragment = SettingsRootFragment()
}