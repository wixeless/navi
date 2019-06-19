package com.marvel.stark.ui.wallets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marvel.stark.R
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.ui.dialog.AddWalletDialog
import kotlinx.android.synthetic.main.fragment_wallets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**Created by Jahongir on 6/15/2019.*/

class WalletsFragment : Fragment(), CoroutineScope, Injectable {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            testRequest()
        }
    }

    private fun testRequest() {
        val walletAddDialog = AddWalletDialog()
        walletAddDialog.show(childFragmentManager, "AddNewWallet")
    }
}