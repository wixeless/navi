package com.marvel.stark.common

import kotlinx.coroutines.CoroutineScope

/**Created by Jahongir on 8/18/2019.*/

abstract class BaseRepository {

    protected lateinit var viewModelScope: CoroutineScope

    fun initScope(viewModelScope: CoroutineScope) {
        this.viewModelScope = viewModelScope
    }
}