package ru.nblackie.dictionary.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nblackie.core.impl.fragment.ContainerFragment
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.di.AppComponent

/**
 * @author tatarchukilya@gmail.com
 */
class NavigationActivity : AppCompatActivity() {

    private lateinit var navigationView: BottomNavigationView
    private lateinit var currentFragment: ContainerFragment

    private val dictionaryFragment: ContainerFragment by lazy {
        AppComponent.get().getDictionaryApi().containerFragment()
    }

    private val exerciseFragment: ContainerFragment =
        AppComponent.get().exerciseFeatureApi().containerFragment()


    private val settingsFragment: ContainerFragment by lazy {
        AppComponent.get().settingsFeatureApi().containerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.nav_host_container, dictionaryFragment, "DictionaryTag")
            }
            currentFragment = dictionaryFragment
        }
        navigationView = findViewById(R.id.bottom_nav)
        navigationView.setOnItemSelectedListener {
            selectTab(it.itemId)
            true
        }
        navigationView.setOnItemReselectedListener {
            fragmentById(it.itemId).reselect()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.nav_host_container) as ContainerFragment
        if (!currentFragment.navigateUp()) {
            super.onBackPressed()
        }
    }

    private fun selectTab(itemId: Int) {
        with(supportFragmentManager) {
            val fragment = obtainFragment(itemId)
            if (fragment != currentFragment) {
                commitNow {
                    detach(currentFragment)
                    attach(fragment)
                }
                currentFragment = fragment
            }
        }
    }

    private fun FragmentManager.obtainFragment(itemId: Int): ContainerFragment {
        val tag = fragmentTag(itemId)
        val existingFragment = findFragmentByTag(tag) as ContainerFragment?
        existingFragment?.let {
            return it
        }
        val newFragment = fragmentById(itemId)
        commitNow { add(R.id.nav_host_container, newFragment, tag) }
        return newFragment
    }

    private fun fragmentById(itemId: Int): ContainerFragment {
        return when (itemId) {
            R.id.item_dictionary -> dictionaryFragment
            R.id.item_exercise -> exerciseFragment
            R.id.item_settings -> settingsFragment
            else -> throw IllegalArgumentException("Illegal item id $itemId")
        }
    }

    private fun fragmentTag(itemId: Int): String {
        return when (itemId) {
            R.id.item_dictionary -> "DictionaryTag"
            R.id.item_exercise -> "ExerciseTag"
            R.id.item_settings -> "SettingsTag"
            else -> throw IllegalArgumentException("Illegal item id $itemId")
        }
    }
}