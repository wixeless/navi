package com.marvel.stark.di

import android.app.Application
import androidx.room.Room
import com.marvel.stark.models.DATABASE_NAME
import com.marvel.stark.room.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Jahongir on 6/15/2019.
 */

@Module(includes = [ViewModelModule::class])
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room
                .databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @Singleton
    fun provideWalletDao(database: AppDatabase): WalletDao {
        return database.walletDao()
    }

    @Provides
    @Singleton
    fun provideWorkerDao(database: AppDatabase): WorkerDao {
        return database.workerDao()
    }

    @Provides
    @Singleton
    fun providePayoutDao(database: AppDatabase): PayoutDao {
        return database.payoutDao()
    }
}