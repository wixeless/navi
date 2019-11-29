package com.marvel.stark.rest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**Created by Jahongir on 6/15/2019.*/

//General model with status and data
@JsonClass(generateAdapter = true)
data class RestData<T>(var status: String = "", var error: String = "", @field:Json(name = "data") val data: T)