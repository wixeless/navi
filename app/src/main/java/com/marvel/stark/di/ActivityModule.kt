package com.marvel.stark.di

import com.marvel.stark.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Jahongir on 6/15/2019.
 */

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}