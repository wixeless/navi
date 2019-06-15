package com.marvel.stark.di

import android.app.Application
import com.marvel.stark.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        ActivityModule::class,
        DatabaseModule::class,
        NetworkModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun baseUrl(url: String): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

}