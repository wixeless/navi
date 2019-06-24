package com.marvel.stark.ui.wallets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marvel.stark.R
import com.marvel.stark.adapter.WalletsAdapter
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.models.Status.*
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

    private val walletsAdapter = WalletsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWalletsRv()
        walletsViewModel.wallets.observe(this, Observer { resource ->
            when (resource.status) {
                SUCCESS -> {
                    resource.data?.let {
                        walletsAdapter.update(it)
                    }
                }
                ERROR -> toastMessage(context, resource.message)
                LOADING -> {
                    resource.data?.let {
                        walletsAdapter.update(it)
                    }
                }
            }
            Log.d("WalletsFragment", "onViewCreated: ${resource.status}")
            resource.data?.forEach {
                Log.d("WalletsFragment", "onViewCreated: ${it.name} ${it.lastSeen}")
            }
            Log.d("WalletsFragment", "onViewCreated: #############################")
        })
        walletsViewModel.errorMessages.observe(this, Observer { message ->
            toastMessage(context, message)
            Log.d("WalletsFragment", "onViewCreated ERROR: $message")
        })
        fab.setOnClickListener {
            showAddWalletDialog()
        }
    }

    private fun setupWalletsRv() {
        wallets_rv.layoutManager = LinearLayoutManager(context)
        wallets_rv.adapter = walletsAdapter
        walletsAdapter.setWalletClickListener { wallet ->
            val action = WalletsFragmentDirections.actionNext(wallet.id)
            findNavController().navigate(action)
        }
    }

    private fun showAddWalletDialog() {
        val walletAddDialog = AddWalletDialog()
        walletAddDialog.show(childFragmentManager, "AddNewWallet")
    }
}