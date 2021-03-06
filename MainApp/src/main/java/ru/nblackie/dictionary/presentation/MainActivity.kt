package ru.nblackie.dictionary.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.di.AppComponent

/**
 * @author tatarchukilya@gmail.com
 */
class MainActivity : AppCompatActivity() {

//    private val tabs by lazy {
//        listOf(R.navigation.dictionary, R.navigation.exersice, R.navigation.settings)
//    }

    private val items by lazy {
        listOf(R.id.item_dictionary, R.id.item_exercise, R.id.item_settings)
    }

    private val dictionaryFragment: NavHostFragment by lazy {
        AppComponent.get().getDictionaryApi().navHostFragment()
    }

    private val exerciseFragment: NavHostFragment by lazy {
        AppComponent.get().exerciseFeatureApi().navHostFragment()
    }

    private val settingsFragment: NavHostFragment by lazy {
        AppComponent.get().settingsFeatureApi().navHostFragment()
    }

    private lateinit var tabStack: TabStack

    private var selectedFragment: NavHostFragment? = null
        set(value) {
            field = value
            field?.let {
                currentNavController = it.navController
            }
        }

    private lateinit var currentNavController: NavController
    private lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView = findViewById(R.id.bottom_nav)
        tabStack = TabStack(savedInstanceState?.getIntegerArrayList(KEY_STACK) ?: arrayListOf())
        supportFragmentManager.findFragmentById(R.id.nav_host_container)?.let {
            selectedFragment = it as NavHostFragment
        } ?: selectTab(0, false)

        navigationView.setOnItemSelectedListener {
            return@setOnItemSelectedListener if (it.itemId == navigationView.selectedItemId) {
                false // ?????????? ???? ???????? ???????????????????????? ?????? ???????????? selectTab ???? onBackPressed
            } else {
                selectTab(items.indexOf(it.itemId), false)
                true
            }
        }

        navigationView.setOnItemReselectedListener {
            selectedFragment
            Log.i("<>", "${currentNavController.currentDestination?.label}")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList(KEY_STACK, tabStack.list)
    }

    override fun onBackPressed() {
        if (!currentNavController.navigateUp()) {
            tabStack.pop().let {
                if (it < 0) finish() else selectTab(it, true)
            }
        }
    }

    private fun selectTab(index: Int, isBack: Boolean) {
        with(supportFragmentManager) {
            val navFragment = obtainNavHostFragment(index)
            if (navFragment != selectedFragment) {
                commitNow {
                    selectedFragment?.let {
                        detach(it)
                    }
                    attach(navFragment)
                }
                if (isBack) navigationView.selectedItemId = items[index] else tabStack.put(index)
                currentNavController = navFragment.navController
                selectedFragment = navFragment
            }
        }
    }

    /**
     * ???????? ???????????????? ?? ?????????? ???????? ?? FragmentManager, ???????????? ??????, ?????????? ?????????????? ??????????
     */
    private fun FragmentManager.obtainNavHostFragment(index: Int): NavHostFragment {
        val tag = getFragmentTag(index)
        val existingFragment = findFragmentByTag(tag) as NavHostFragment?
        existingFragment?.let {
            return it
        }
        val newFragment = fragmentByIndex(index)
        commitNow { add(R.id.nav_host_container, newFragment, tag) }
        return newFragment
    }

    private fun fragmentByIndex(index: Int): NavHostFragment {
        return when (index) {
            0 -> dictionaryFragment
            1 -> exerciseFragment
            2 -> settingsFragment
            else -> throw IllegalArgumentException("Illegal fragment index $index")
        }
    }

    /**
     * ???????????? ???????????????????????????????????? ?????????????????? ???? ???????????????????? ?????????? [onBackPressed]
     */
    private inner class TabStack(val list: ArrayList<Int>) {

        fun pop(): Int {
            if (list.size > 1) {
                list.removeLast()
                return list.last()
            }
            return -1
        }

        fun put(tabIndex: Int) {
            when (list.indexOf(tabIndex)) {
                -1 -> list.add(tabIndex) // ???????????? ??????, ???????????? ????????????????
                0 -> list.removeAll { it > 0 } // Home, ???????????????? ???????????? ?????? (index = 0, element = 0)
                else -> {
                    list.remove(tabIndex) // ???????? ?????????? ?????? ????????????????, ?????????????????????? ?? ?????????? ????????????
                    list.add(tabIndex)
                }
            }
        }
    }

    private fun getFragmentTag(index: Int) = "navigationFragment#$index"

    private companion object {
        const val KEY_STACK = "tab_stack"
    }
}