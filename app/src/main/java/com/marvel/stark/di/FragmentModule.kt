package com.marvel.stark.di

import com.marvel.stark.ui.settings.WalletSettingsFragment
import com.marvel.stark.ui.dialog.WalletAddDialog
import com.marvel.stark.ui.payout.PayoutFragment
import com.marvel.stark.ui.home.HomeFragment
import com.marvel.stark.ui.worker.WorkerFragment
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
    abstract fun contributeAddWalletDialog(): WalletAddDialog

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributePayoutFragment(): PayoutFragment

    @ContributesAndroidInjector
    abstract fun contributeWorkerFragment(): WorkerFragment

    @ContributesAndroidInjector
    abstract fun contributeWalletSettingsFragment(): WalletSettingsFragment
}