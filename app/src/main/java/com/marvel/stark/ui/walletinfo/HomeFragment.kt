package com.marvel.stark.ui.walletinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marvel.stark.R
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.utils.putArgs
import java.lang.RuntimeException

/**Created by Jahongir on 6/22/2019.*/

class HomeFragment : Fragment(), Injectable {

    companion object {
        private val ARGS_BUNDLE = HomeFragment::class.java.name + ":Bundle"
        fun newInstance(walletId: Long) = HomeFragment().putArgs {
            putLong(ARGS_BUNDLE, walletId)
        }
    }

    private val walletId: Long by lazy {
        arguments?.getLong(ARGS_BUNDLE) ?: throw RuntimeException("Wallet id missing")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("HomeFragment", "onViewCreated: $walletId")
    }
}