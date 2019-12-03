package com.marvel.stark.ui.payout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.marvel.stark.R
import com.marvel.stark.adapter.PayoutsAdapter
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.shared.di.ViewModelFactory
import com.marvel.stark.shared.result.Status.*
import com.marvel.stark.ui.SharedViewModel
import kotlinx.android.synthetic.main.fragment_payout.*
import javax.inject.Inject

/**Created by Jahongir on 6/22/2019.*/

class PayoutFragment : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val payoutsAdapter = PayoutsAdapter()

    private val viewModel: PayoutViewModel by viewModels { viewModelFactory }
    private val sharedViewModel: SharedViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_payout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycleView()
        sharedViewModel.wallet.observe(viewLifecycleOwner, Observer {
            viewModel.setWallet(wallet = it)
        })
        viewModel.payouts.observe(this, Observer { resource ->
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