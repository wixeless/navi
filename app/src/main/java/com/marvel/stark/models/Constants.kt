package com.marvel.stark.models

import kotlinx.coroutines.Dispatchers

/**Created by Jahongir on 7/13/2019.*/

val uiDispatcher = Dispatchers.Main
val bgDispatcher = Dispatchers.IO
const val DATABASE_NAME = "navi"

const val updateThreshold: Long = 600000//This is 10 min in millis