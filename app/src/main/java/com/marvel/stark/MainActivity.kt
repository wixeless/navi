package com.marvel.stark

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.idescout.sql.SqlScoutServer
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.ui.ToolbarViewModel
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

    private val toolbarViewModel: ToolbarViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SqlScoutServer.create(this, packageName)
        setupNavigation()
        subscribeToolbarTitle()
    }

    private fun subscribeToolbarTitle() {
        //val toolbarViewModel = ViewModelProviders.of(this).get(ToolbarViewModel::class.java)
        toolbarViewModel.title.observe(this, Observer {
            toolbar.title = it
        })
    }

    private fun setupNavigation() {
        val hostFragment: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.host_fragment) as NavHostFragment? ?: return
        val navController = hostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.host_fragment).navigateUp(appBarConfiguration)
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
