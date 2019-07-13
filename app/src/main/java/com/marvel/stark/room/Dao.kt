package com.marvel.stark.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*
import com.marvel.stark.models.DashboardDto


/**Created by Jahongir on 6/15/2019.*/

@Dao
interface WalletDao : BaseDao<Wallet> {

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
abstract class DashboardDao {

    @WorkerThread
    fun insert(dashboard: DashboardDto) {
        dashboard.wallet.lastSeen = System.currentTimeMillis()
        val walletId = insertWallet(dashboard.wallet)
        dashboard.workers.forEach { it.walletId = walletId }
        insertWorkers(dashboard.workers)
    }

    @WorkerThread
    @Transaction
    open fun update(wallet: Wallet, dashboard: DashboardDto) {
        dashboard.setWallet(wallet)
        dashboard.wallet.lastSeen = System.currentTimeMillis()
        dashboard.workers.forEach { it.walletId = dashboard.wallet.id }
        deleteWorkers(dashboard.wallet.id)
        updateWallet(dashboard.wallet)
        insertWorkers(dashboard.workers)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertWallet(wallet: Wallet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertWorkers(workersList: List<Worker>)

    @Query("DELETE FROM worker WHERE wallet_id=:walledId")
    abstract fun deleteWorkers(walledId: Long)

    @Update
    abstract fun updateWallet(wallet: Wallet)
}

@Dao
interface WorkerDao : BaseDao<Worker> {
    @Query("SELECT * FROM worker WHERE wallet_id=:walledId")
    fun getWorkers(walledId: Long): LiveData<List<Worker>>
}

@Dao
abstract class PayoutDao {
    @Query("SELECT * FROM payout WHERE wallet_id=:walledId")
    abstract fun getPayouts(walledId: Long): LiveData<List<Payout>>

    @WorkerThread
    @Transaction
    open fun insert(walledId: Long, list: List<Payout>) {
        deletePayouts(walledId)
        insertPayouts(list)
    }

    @Query("DELETE FROM payout WHERE wallet_id=:walledId")
    abstract fun deletePayouts(walledId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPayouts(list: List<Payout>)
}


