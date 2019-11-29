package com.marvel.stark.ui.walletinfo.payout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.marvel.stark.R
import com.marvel.stark.adapter.PayoutsAdapter
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.models.Status.*
import com.marvel.stark.utils.putArgs
import kotlinx.android.synthetic.main.fragment_payout.*
import javax.inject.Inject

/**Created by Jahongir on 6/22/2019.*/

class PayoutFragment : Fragment(), Injectable {

    companion object {
        private val ARGS_BUNDLE = PayoutFragment::class.java.name + ":Bundle"
        fun newInstance(walletId: Long) = PayoutFragment().putArgs {
            putLong(ARGS_BUNDLE, walletId)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val payoutsAdapter = PayoutsAdapter()

    private val payoutViewModel: PayoutViewModel by viewModels { viewModelFactory }

    private val walletId: Long by lazy {
        arguments?.getLong(ARGS_BUNDLE) ?: throw RuntimeException("Wallet id missing")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_payout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycleView()
        payoutViewModel.setWalletId(walletId)
        payoutViewModel.payouts.observe(this, Observer { resource ->
            when (resource.status) {
                SUCCESS -> {
                    resource.data?.let {
                        payoutsAdapter.update(it)
                    }
                }
                ERROR -> Log.d("PayoutFragment", "onViewCreated: ERROR: ${resource.message}")
                LOADING -> {
                    resource.data?.let {
                        payoutsAdapter.update(it)
                    }
                }
            }
        })
    }

    private fun setupRecycleView() {
        payouts_rv.layoutManager = LinearLayoutManager(context)
        payouts_rv.adapter = payoutsAdapter
    }
}