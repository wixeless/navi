package com.marvel.stark.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.di.factory.ViewModelKey
import com.marvel.stark.ui.dialog.AddWalletViewModel
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
    @ViewModelKey(AddWalletViewModel::class)
    abstract fun bindAddWalletViewModel(addWalletViewModel: AddWalletViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WalletsViewModel::class)
    abstract fun bindWalletsViewModel(walletsViewModel: WalletsViewModel): ViewModel
}