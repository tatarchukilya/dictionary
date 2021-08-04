package ru.nblackie.dictionary.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nblackie.dictionary.R

/**
 * @author tatarchukilya@gmail.com
 */
class MainActivity : AppCompatActivity() {

    private val tabs by lazy {
        listOf(R.navigation.search, R.navigation.exersice, R.navigation.settings)
    }

    private val items by lazy {
        listOf(R.id.item_dictionary, R.id.item_exercise, R.id.item_settings)
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
                false
            } else {
                selectTab(items.indexOf(it.itemId), false)
                true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList(KEY_STACK, tabStack.list)
    }

    override fun onBackPressed() {
        if (!currentNavController.navigateUp()) {
            tabStack.pop().let { if (it < 0) finish() else selectTab(it, true) }
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
     * Если фрагмент с тэгом есть в FragmentManager, вернет его, иначе создаст новый
     */
    private fun FragmentManager.obtainNavHostFragment(index: Int): NavHostFragment {
        val tag = getFragmentTag(index)
        val existingFragment = findFragmentByTag(tag) as NavHostFragment?
        existingFragment?.let {
            return it
        }
        val newFragment = NavHostFragment.create(tabs[index])
        commitNow { add(R.id.nav_host_container, newFragment, tag) }
        return newFragment
    }

    /**
     * Хранит последовательность переходов по вкладкам через [onBackPressed]
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
                -1 -> list.add(tabIndex) // Такого нет, просто добавить
                0 -> list.removeAll { it > 0 } // Home, оставить только его (index = 0, element = 0)
                else -> {
                    list.remove(tabIndex) // Если такой уже добавлен, переместить в конец списка
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