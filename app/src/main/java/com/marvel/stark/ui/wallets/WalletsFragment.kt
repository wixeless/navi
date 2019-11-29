package com.marvel.stark.ui.wallets

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marvel.stark.R
import com.marvel.stark.adapter.WalletsAdapter
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.shared.di.ViewModelFactory
import com.marvel.stark.shared.result.Status.ERROR
import com.marvel.stark.shared.result.Status.LOADING
import com.marvel.stark.utils.toastMessage
import kotlinx.android.synthetic.main.fragment_wallets.*
import javax.inject.Inject

/**Created by Jahongir on 6/15/2019.*/

class WalletsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val walletsViewModel: WalletsViewModel by viewModels { viewModelFactory }

    private val walletsAdapter = WalletsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWalletsRv()
        walletsViewModel.wallets.observe(this, Observer { resource ->

            wallets_refresh.isRefreshing = resource.status == LOADING

            if (resource.status == ERROR)
                toastMessage(context, resource.message)

            resource.data?.let {
                walletsAdapter.update(it)
            }
        })

        walletsViewModel.errorMessages.observe(this, Observer { message ->
            toastMessage(context, message)
        })

        fab.setOnClickListener {
            val action = WalletsFragmentDirections.actionAddWallet()
            findNavController().navigate(action)
        }

        wallets_refresh.setOnRefreshListener {
            walletsViewModel.refresh()
        }
    }

    override fun onResume() {
        Handler().postDelayed({
            try {
                fab.show()
            } catch (skip: NullPointerException) {
            }
        }, 400)
        super.onResume()
    }

    private fun setupWalletsRv() {
        wallets_rv.layoutManager = LinearLayoutManager(context)
        wallets_rv.adapter = walletsAdapter
        walletsAdapter.setWalletClickListener { wallet ->
            fab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                override fun onHidden(fab: FloatingActionButton?) {
                    val action = WalletsFragmentDirections.actionOpenWallet(wallet.id)
                    findNavController().navigate(action)
                }
            })
        }
    }
}