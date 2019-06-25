package com.marvel.stark.ui.walletinfo.earning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marvel.stark.R
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.utils.putArgs

/**Created by Jahongir on 6/22/2019.*/

class EarningFragment : Fragment(), Injectable {

    companion object {
        private val ARGS_BUNDLE = EarningFragment::class.java.name + ":Bundle"
        fun newInstance(walletId: Long) = EarningFragment().putArgs {
            putLong(ARGS_BUNDLE, walletId)
        }
    }

    private val walletId: Long by lazy {
        arguments?.getLong(ARGS_BUNDLE) ?: throw RuntimeException("Wallet id missing")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_earning, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Log.d("EarningFragment", "onViewCreated: $walletId")
    }
}