package com.marvel.stark.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.marvel.stark.R
import com.marvel.stark.models.uiDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**Created by Jahongir on 6/18/2019.*/


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


object Formatter{

    fun hashrate(hash: Long): String {
        val h = "H/s"
        val kh = "kH/s"
        val mh = "MH/s"
        val gh = "GH/s"
        val th = "TH/s"
        return when (hash) {
            in 0..1000 -> {
                "$hash $h"
            }
            in 1000..1000000 -> {
                val v = hash.div(1000.0)
                val s = "%.1f".format(v)
                "$s $kh"
            }
            in 1000000..1000000000 -> {
                val v = hash.div(1000000.0)
                val s = "%.1f".format(v)
                "$s $mh"
            }
            in 1000000000..1000000000000 -> {
                val v = hash.div(1000000000.0)
                val s = "%.1f".format(v)
                "$s $gh"
            }
            else -> {
                val v = hash.div(1000000000000.0)
                val s = "%.1f".format(v)
                "$s $th"
            }
        }
    }

    fun unpaid(unpaid: Long): String {
        val one = 1000000000000000000.0
        val holder = unpaid.div(one)
        return "%.5f".format(holder)
    }
}

fun formatHashrate(hash2: Double): String {
    val hash = hash2.toLong()
    val h = "H/s"
    val kh = "kH/s"
    val mh = "MH/s"
    val gh = "GH/s"
    val th = "TH/s"
    return when (hash) {
        in 0..1000 -> {
            "$hash $h"
        }
        in 1000..1000000 -> {
            val v = hash.div(1000.0)
            val s = "%.1f".format(v)
            "$s $kh"
        }
        in 1000000..1000000000 -> {
            val v = hash.div(1000000.0)
            val s = "%.1f".format(v)
            "$s $mh"
        }
        in 1000000000..1000000000000 -> {
            val v = hash.div(1000000000.0)
            val s = "%.1f".format(v)
            "$s $gh"
        }
        else -> {
            val v = hash.div(1000000000000.0)
            val s = "%.1f".format(v)
            "$s $th"
        }
    }
}



