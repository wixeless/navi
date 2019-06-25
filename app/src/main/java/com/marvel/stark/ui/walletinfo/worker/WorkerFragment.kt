package com.marvel.stark.ui.walletinfo.worker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.marvel.stark.R
import com.marvel.stark.adapter.WorkersAdapter
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.models.Status.*
import com.marvel.stark.utils.putArgs
import kotlinx.android.synthetic.main.fragment_worker.*
import javax.inject.Inject

/**Created by Jahongir on 6/22/2019.*/

class WorkerFragment : Fragment(), Injectable {

    companion object {
        private val ARGS_BUNDLE = WorkerFragment::class.java.name + ":Bundle"
        fun newInstance(walletId: Long) = WorkerFragment().putArgs {
            putLong(ARGS_BUNDLE, walletId)
        }
    }

    private val walletId: Long by lazy {
        arguments?.getLong(ARGS_BUNDLE) ?: throw RuntimeException("Wallet id missing")
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val workersAdapter = WorkersAdapter()

    private val workerViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(WorkerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_worker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        workerViewModel.setWalletId(walletId)
        workerViewModel.workers.observe(this, Observer { resource ->
            when (resource.status) {
                SUCCESS -> {
                    resource.data?.let {
                        workersAdapter.update(it)
                    }
                }
                ERROR -> Log.d("WorkerFragment", "onViewCreated: ERROR: ${resource.message}")
                LOADING -> {
                    resource.data?.let {
                        workersAdapter.update(it)
                    }
                }
            }
        })
    }

    private fun setupViewPager() {
        workers_rv.layoutManager = GridLayoutManager(context, 2)
        workers_rv.adapter = workersAdapter
    }

}