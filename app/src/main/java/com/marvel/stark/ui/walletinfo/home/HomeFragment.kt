package com.marvel.stark.ui.walletinfo.home

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
import com.marvel.stark.models.Status.*
import com.marvel.stark.room.Wallet
import com.marvel.stark.utils.Formatter
import com.marvel.stark.utils.putArgs
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**Created by Jahongir on 6/22/2019.*/

class HomeFragment : Fragment(), Injectable {

    companion object {
        private val ARGS_BUNDLE = HomeFragment::class.java.name + ":Bundle"
        fun newInstance(walletId: Long) = HomeFragment().putArgs {
            putLong(ARGS_BUNDLE, walletId)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val homeViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
    }


    private val walletId: Long by lazy {
        arguments?.getLong(ARGS_BUNDLE) ?: throw RuntimeException("Wallet id missing")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeViewModel.setWalletId(walletId)
        homeViewModel.wallets.observe(viewLifecycleOwner, Observer { resource ->
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