package com.marvel.stark.room

import androidx.annotation.WorkerThread
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**Created by Jahongir on 6/15/2019.*/

interface BaseDao<T> {
    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T)

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<T>)

    @WorkerThread
    @Update
    suspend fun update(vararg obj: T)

    @WorkerThread
    @Delete
    suspend fun delete(vararg obj: T)
}