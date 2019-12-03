package com.marvel.stark.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*


/**Created by Jahongir on 6/15/2019.*/

@Dao
interface WalletDao {

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: Wallet): Long

    @WorkerThread
    @Update
    suspend fun update(wallet: Wallet)

    @Query("SELECT * FROM wallet")
    fun getWallets(): LiveData<List<Wallet>>

    @Query("SELECT * FROM wallet WHERE id=:walledId")
    fun getWalletLiveData(walledId: Long): LiveData<Wallet>

    @WorkerThread
    @Query("SELECT * FROM wallet WHERE id=:walledId")
    fun getWallet(walledId: Long): Wallet

    @Query("DELETE FROM wallet WHERE id=:walledId")
    fun deleteById(walledId: Long)

    @Query("SELECT * FROM wallet")
    fun getWalletsList(): List<Wallet>
}

@Dao
interface WorkerDao : BaseDao<Worker> {
    @Query("SELECT * FROM worker WHERE wallet_id=:walledId")
    fun getWorkers(walledId: Long): LiveData<List<Worker>>

    @WorkerThread
    @Transaction
    suspend fun cleanInsert(walledId: Long, workers: List<Worker>) {
        deleteWorkers(walledId = walledId)
        workers.forEach { it.walletId = walledId }
        insertList(list = workers)
    }

    @Query("DELETE FROM worker WHERE wallet_id=:walledId")
    suspend fun deleteWorkers(walledId: Long)
}

@Dao
abstract class PayoutDao {
    @Query("SELECT * FROM payout WHERE wallet_id=:walledId")
    abstract fun getPayouts(walledId: Long): LiveData<List<Payout>>

    @WorkerThread
    @Transaction
    open fun insert(walletId: Long, payouts: List<Payout>) {
        deletePayouts(walletId)
        payouts.forEach { it.walletId = walletId }
        insertPayouts(payouts)
    }

    @Query("DELETE FROM payout WHERE wallet_id=:walledId")
    abstract fun deletePayouts(walledId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPayouts(list: List<Payout>)
}


