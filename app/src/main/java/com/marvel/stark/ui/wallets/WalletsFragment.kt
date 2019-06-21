package com.marvel.stark.ui.wallets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.marvel.stark.R
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.ui.dialog.AddWalletDialog
import com.marvel.stark.utils.toastMessage
import kotlinx.android.synthetic.main.fragment_wallets.*
import javax.inject.Inject

/**Created by Jahongir on 6/15/2019.*/

class WalletsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val walletsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(WalletsViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletsViewModel.wallets.observe(this, Observer { resource ->
            Log.d("WalletsFragment", "onViewCreated: ${resource.status}")
            resource.data?.forEach {
                Log.d("WalletsFragment", "onViewCreated: ${it.name} ${it.lastSeen}")
            }
            Log.d("WalletsFragment", "onViewCreated: #############################")
        })
        walletsViewModel.errorMessages.observe(this, Observer {
            toastMessage(context, it)
            Log.d("WalletsFragment", "onViewCreated ERROR: $it")
        })
        fab.setOnClickListener {
            showAddWalletDialog()
        }
    }

    private fun showAddWalletDialog() {
        val walletAddDialog = AddWalletDialog()
        walletAddDialog.show(childFragmentManager, "AddNewWallet")
    }
}