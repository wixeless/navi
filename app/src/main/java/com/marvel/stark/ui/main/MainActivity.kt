package com.marvel.stark.ui.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.idescout.sql.SqlScoutServer
import com.marvel.stark.R
import com.marvel.stark.shared.di.ViewModelFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Jahongir on 6/14/2019.
 */
class MainActivity : AppCompatActivity(), CoroutineScope, HasAndroidInjector {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val bottomNavFrags = setOf(R.id.home_dest, R.id.worker_dest, R.id.payout_dest)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SqlScoutServer.create(this, packageName)
        setupNavigation()
    }

    private fun setupNavigation() {
        val hostFragment: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.host_fragment) as NavHostFragment? ?: return
        val navController = hostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            changeBottomNavigationState(destination.id in bottomNavFrags)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.host_fragment).navigateUp(appBarConfiguration)
    }

    private fun changeBottomNavigationState(show: Boolean) {

        if (show) {
            if (bottomNavigation.visibility == View.VISIBLE)
                return
        } else {
            if (bottomNavigation.visibility == View.GONE)
                return
        }
        val height = bottomNavigation.height.toFloat()
        val animator: ObjectAnimator
        animator = if (show) {
            ObjectAnimator.ofFloat(bottomNavigation, "translationY", height, 0f)
        } else {
            ObjectAnimator.ofFloat(bottomNavigation, "translationY", 0f, height)
        }

        animator.duration = 500
        animator.addListener(
                onStart = {
                    if (show) {
                        bottomNavigation.visibility = View.VISIBLE
                    }
                },
                onEnd = {
                    if (!show) {
                        bottomNavigation.visibility = View.GONE
                    }
                }
        )
        animator.start()
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
