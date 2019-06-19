package com.marvel.stark.di

import com.marvel.stark.ui.ViewPagerFragment
import com.marvel.stark.ui.WalletSettingsFragment
import com.marvel.stark.ui.dialog.AddWalletDialog
import com.marvel.stark.ui.dialog.AddWalletRepository
import com.marvel.stark.ui.wallets.WalletsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Jahongir on 6/15/2019.
 */

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeWalletsFragment(): WalletsFragment

    @ContributesAndroidInjector
    abstract fun contributeViewPagerFragment(): ViewPagerFragment

    @ContributesAndroidInjector
    abstract fun contributeWalletSettingsFragment(): WalletSettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeAddWalletDialog(): AddWalletDialog

    @ContributesAndroidInjector
    abstract fun contributeAddWalletRepository(): AddWalletRepository

}