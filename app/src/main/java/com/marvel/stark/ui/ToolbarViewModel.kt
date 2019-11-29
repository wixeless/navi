package com.marvel.stark.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**Created by Jahongir on 6/25/2019.*/

class ToolbarViewModel @Inject constructor() : ViewModel() {
    val title = MutableLiveData<String>()
}