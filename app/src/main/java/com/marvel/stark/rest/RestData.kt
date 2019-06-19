package com.marvel.stark.rest

import com.marvel.stark.room.Wallet
import com.marvel.stark.room.Worker
import com.squareup.moshi.Json

/**Created by Jahongir on 6/15/2019.*/

//General model with status and data
data class RestData<T>(var status: String, var error: String, @field:Json(name = "data") val data: T)