package com.marvel.stark.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marvel.stark.R
import com.marvel.stark.di.factory.Injectable
import com.marvel.stark.di.factory.ViewModelFactory
import com.marvel.stark.models.Status.*
import com.marvel.stark.utils.toastMessage
import kotlinx.android.synthetic.main.dialog_add_wallet.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**Created by Jahongir on 6/18/2019.*/

class AddWalletDialog : BottomSheetDialogFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val walletViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AddWalletViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_add_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletViewModel.addWalletResult.observe(this, Observer {
            val msg = when (it.status) {
                SUCCESS -> "SUCCESS CLOSE"
                ERROR -> it.message
                LOADING -> "SHOW LOADING"
            }
            showMsh(msg)
        })
        wallet_fab.setOnClickListener { test() }
    }

    private fun test() {
        walletViewModel.onAddWallet("0x3ce72e8c2245c30d1cd340effc38ef64338c1ccb")
    }

    private fun showMsh(msg: String?) {
        val sdf = SimpleDateFormat("hh:mm:ss:SSS", Locale.getDefault())
        val currentDate = sdf.format(Date())
        Log.d("AddWalletDialog", "showMsh $currentDate: $msg")
        toastMessage(context, msg)
    }
}