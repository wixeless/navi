package com.marvel.stark.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Created by Jahongir on 6/15/2019.
 */

@Database(entities = [Wallet::class, Worker::class, Payout::class],
        version = 9, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun workerDao(): WorkerDao
    abstract fun payoutDao(): PayoutDao
}