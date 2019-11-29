package com.marvel.stark.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.marvel.stark.R
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.models.Status.*
import com.marvel.stark.room.Wallet
import com.marvel.stark.ui.SharedViewModel
import com.marvel.stark.utils.Formatter
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**Created by Jahongir on 6/22/2019.*/

class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private val sharedViewModel: SharedViewModel by activityViewModels { viewModelFactory }

    private val safeArgs: HomeFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (safeArgs.walletId > 0)
            sharedViewModel.setWalletId(safeArgs.walletId)
        sharedViewModel.walletId.observe(viewLifecycleOwner, Observer {
            viewModel.setWalletId(walletId = it)
        })
        viewModel.wallets.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                SUCCESS -> {
                    resource.data?.let {
                        setWalletInfo(it)
                    }
                    Log.d("HomeFragment", "onViewCreated SUCCESS: ${resource.data}")
                }
                ERROR -> {
                    Log.d("HomeFragment", "onViewCreated ERROR: ${resource.message}")
                }
                LOADING -> {
                    Log.d("HomeFragment", "onViewCreated: LOADING ${resource.data}")
                    resource.data?.let {
                        setWalletInfo(it)
                    }
                }
            }
        })
    }

    private fun setWalletInfo(wallet: Wallet) {
        val unpaidStr = "${Formatter.unpaid(wallet.unpaid)} ${wallet.coin}"
        unpaid.text = unpaidStr
        workers.text = wallet.activeWorkers.toString()
        hashrateReported.text = Formatter.hashrate(wallet.reportedHashrate)
        hashrateCurrent.text = Formatter.hashrate(wallet.currentHashrate)
    }
}