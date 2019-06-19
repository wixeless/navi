package com.marvel.stark.room

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.marvel.stark.models.DashboardDto

/**Created by Jahongir on 6/15/2019.*/

@Dao
interface WalletDao : BaseDao<Wallet> {

}

@Dao
interface WorkerDao : BaseDao<Worker> {
}

@Dao
abstract class DashboardDao {

    @WorkerThread
    fun insert(dashboard: DashboardDto) {
        val walletId = insertWallet(dashboard.wallet)
        dashboard.workers.forEach { it.walletId = walletId }
        insertWorkers(dashboard.workers)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertWallet(wallet: Wallet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertWorkers(workersList: List<Worker>)
}
