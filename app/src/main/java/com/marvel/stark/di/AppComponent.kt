package com.marvel.stark.di

import android.app.Application
import com.marvel.stark.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
        modules = [AndroidInjectionModule::class,
            ActivityModule::class,
            DatabaseModule::class,
            NetworkModule::class]
)

interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application,
                   @BindsInstance baseUrl: String): AppComponent
    }
}