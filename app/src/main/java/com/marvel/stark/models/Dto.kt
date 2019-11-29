package com.marvel.stark.models

import com.marvel.stark.room.Wallet
import com.marvel.stark.room.Worker
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**Created by Jahongir on 6/19/2019.*/
@JsonClass(generateAdapter = true)
data class DashboardDto(@field:Json(name = "currentStatistics") val wallet: Wallet,
                        @field:Json(name = "workers") val workers: List<Worker>) {
    fun setWallet(wallet: Wallet) {
        this.wallet.id = wallet.id
        this.wallet.name = wallet.name
        this.wallet.coin = wallet.coin
        this.wallet.address = wallet.address
    }
}

@JsonClass(generateAdapter = true)
data class WalletAddEntity(val address: String = "", val name: String = "")