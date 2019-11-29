package com.marvel.stark.room

import androidx.room.*
import com.marvel.stark.models.Coin
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**Created by Jahongir on 6/15/2019.*/

@Entity(tableName = "wallet")
@JsonClass(generateAdapter = true)
data class Wallet(@PrimaryKey(autoGenerate = true) var id: Long = 0L,
                  var address: String = "",
                  var coin: Coin = Coin.ETH,
                  var name: String = "",
                  var unpaid: Long = 0L,
                  @ColumnInfo(name = "current_hashrate") var currentHashrate: Long = 0L,
                  @ColumnInfo(name = "reported_hashrate") var reportedHashrate: Long = 0L,
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
@JsonClass(generateAdapter = true)
data class Worker(@PrimaryKey(autoGenerate = true) val id: Long = 0L,
                  @ColumnInfo(name = "wallet_id") var walletId: Long =-1L,
                  @field:Json(name = "worker") val name: String = "",
                  @ColumnInfo(name = "current_hashrate") val currentHashrate: Long =0L,
                  @ColumnInfo(name = "reported_hashrate") val reportedHashrate: Long = 0L,
                  @ColumnInfo(name = "last_seen") val lastSeen: Long = 0L)


@Entity(
        tableName = "payout",
        indices = [Index(value = ["wallet_id"])],
        foreignKeys = [ForeignKey(
                entity = Wallet::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("wallet_id"),
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)]
)
@JsonClass(generateAdapter = true)
data class Payout(@PrimaryKey(autoGenerate = true) val id: Long = 0L,
                  @ColumnInfo(name = "wallet_id") var walletId: Long = -1L,
                  val amount: Long = 0L,
                  val paidOn: Long = 0L)

