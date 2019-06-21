package com.marvel.stark.room

import androidx.room.*
import com.marvel.stark.models.Coin
import com.squareup.moshi.Json

/**Created by Jahongir on 6/15/2019.*/

@Entity(tableName = "wallet")
data class Wallet(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                  var address: String = "",
                  var coin: Coin = Coin.ETH,
                  var name: String = "",
                  var unpaid: Long = 0L,
                  @ColumnInfo(name = "current_hashrate") var currentHashrate: Double = 0.0,
                  @ColumnInfo(name = "reported_hashrate") var reportedHashrate: Double = 0.0,
                  @ColumnInfo(name = "active_workers") var activeWorkers: Int = 0,
                  @ColumnInfo(name = "last_seen") var lastSeen: Long = 0L)

@Entity(
        tableName = "worker",
        indices = [Index(value = ["wallet_id"])],
        foreignKeys = [ForeignKey(
                entity = Wallet::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("wallet_id"),
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)]
)
data class Worker(@PrimaryKey(autoGenerate = true) val id: Long,
                  @ColumnInfo(name = "wallet_id") var walletId: Long,
                  @field:Json(name = "worker") val name: String,
                  @ColumnInfo(name = "current_hashrate") val currentHashrate: Double,
                  @ColumnInfo(name = "reported_hashrate") val reportedHashrate: Double,
                  @ColumnInfo(name = "last_seen") val lastSeen: Long)

