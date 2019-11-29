package com.marvel.stark.room

import androidx.room.TypeConverter
import com.marvel.stark.models.Coin

/**Created by Jahongir on 6/19/2019.*/

class RoomConverters {
    companion object {
        @JvmStatic
        @TypeConverter
        fun toCoin(coin: String): Coin = Coin.valueOf(coin)

        @JvmStatic
        @TypeConverter
        fun fromCoin(coin: Coin): String = coin.name
    }
}