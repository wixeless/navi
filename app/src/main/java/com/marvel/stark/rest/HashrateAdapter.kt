package com.marvel.stark.rest

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson


/**Created by Jahongir on 6/22/2019.*/

class HashrateAdapter {

    @FromJson
    fun fromJson(hashrate: Double): Long = hashrate.toLong()

    @ToJson
    fun toJson(hashrate: Long): Double = hashrate.toDouble()
}