package com.marvel.stark.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.di.factory.ViewModelKey
import com.marvel.stark.ui.ToolbarViewModel
import com.marvel.stark.ui.dialog.WalletAddViewModel
import com.marvel.stark.ui.walletinfo.home.HomeViewModel
import com.marvel.stark.ui.walletinfo.payout.PayoutViewModel
import com.marvel.stark.ui.walletinfo.worker.WorkerViewModel
import com.marvel.stark.ui.wallets.WalletsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Jahongir on 6/15/2019.
 */

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WalletAddViewModel::class)
    abstract fun bindAddWalletViewModel(walletAddViewModel: WalletAddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WalletsViewModel::class)
    abstract fun bindWalletsViewModel(walletsViewModel: WalletsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WorkerViewModel::class)
    abstract fun bindWorkerViewModel(workerViewModel: WorkerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PayoutViewModel::class)
    abstract fun bindPayoutViewModel(workerViewModel: PayoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ToolbarViewModel::class)
    abstract fun bindToolbarViewModel(toolbarViewModel: ToolbarViewModel): ViewModel

}