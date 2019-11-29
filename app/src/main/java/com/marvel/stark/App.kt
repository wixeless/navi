package com.marvel.stark

import android.app.Application
import com.marvel.stark.di.AppComponent
import com.marvel.stark.di.factory.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Created by Jahongir on 6/15/2019.
 */

class App : Application(), HasAndroidInjector {
    companion object {
        lateinit var component: AppComponent
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        component = AppInjector.init(this)
    }

    override fun androidInjector() = dispatchingAndroidInjector
}