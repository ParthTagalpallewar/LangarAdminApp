package com.android.adminapplication.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.adminapplication.data.models.Volentiere
import com.android.adminapplication.data.roomdb.daos.UserDao


@Database(
    entities = [Volentiere::class],
    version = 1
)
abstract class  AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao



    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "LangarSevaDb"
            ).build()
    }
}