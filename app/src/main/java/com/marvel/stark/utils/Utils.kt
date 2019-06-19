package com.marvel.stark.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.marvel.stark.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**Created by Jahongir on 6/18/2019.*/

val uiDispatcher = Dispatchers.Main
val bgDispatcher = Dispatchers.IO


@SuppressLint("InflateParams")
fun toastMessage(context: Context?, message: String?) = CoroutineScope(uiDispatcher).launch {
    val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout = inflater.inflate(R.layout.toast_message, null)
    val text = layout.findViewById(R.id.text) as TextView
    val msg = message ?: "NULL MESSAGE"
    text.text = msg
    val toast = Toast(context)
    toast.duration = Toast.LENGTH_LONG
    toast.view = layout
    toast.show()
}