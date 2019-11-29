package com.marvel.stark.ui.worker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.marvel.stark.R
import com.marvel.stark.adapter.WorkersAdapter
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.shared.result.Status.*
import com.marvel.stark.ui.SharedViewModel
import kotlinx.android.synthetic.main.fragment_worker.*
import javax.inject.Inject

/**Created by Jahongir on 6/22/2019.*/

class WorkerFragment : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val workersAdapter = WorkersAdapter()

    private val viewModel: WorkerViewModel by viewModels { viewModelFactory }

    private val sharedViewModel: SharedViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_worker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        sharedViewModel.walletId.observe(viewLifecycleOwner, Observer {
            viewModel.setWalletId(walletId = it)
        })
        viewModel.workers.observe(viewLifecycleOwner, Observer { resource ->
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