package com.marvel.stark.di

import com.marvel.stark.ui.walletinfo.ViewPagerFragment
import com.marvel.stark.ui.WalletSettingsFragment
import com.marvel.stark.ui.dialog.AddWalletDialog
import com.marvel.stark.ui.walletinfo.payout.PayoutFragment
import com.marvel.stark.ui.walletinfo.home.HomeFragment
import com.marvel.stark.ui.walletinfo.worker.WorkerFragment
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
    abstract fun contributeAddWalletDialog(): AddWalletDialog

    @ContributesAndroidInjector
    abstract fun contributeViewPagerFragment(): ViewPagerFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributePayoutFragment(): PayoutFragment

    @ContributesAndroidInjector
    abstract fun contributeWorkerFragment(): WorkerFragment

    @ContributesAndroidInjector
    abstract fun contributeWalletSettingsFragment(): WalletSettingsFragment
}