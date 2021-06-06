package com.android.adminapplication.data.roomdb.daos

import androidx.room.*
import com.android.adminapplication.data.models.Volentiere
import com.android.adminapplication.data.models.Volentiere.Companion.Volentiere_Primary_key
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("Select * FROM user_table WHERE uId = :id")
    fun getCurrentUser(id: Int = Volentiere_Primary_key): Volentiere?

    @Query("Select * FROM `user_table` WHERE uId = :id")
    fun getCurrentUserFlow(id: Int = Volentiere_Primary_key): Flow<Volentiere?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(volentiere: Volentiere):Long

    @Update
    fun updateUser(volentiere: Volentiere): Int

    @Query("UPDATE USER_TABLE SET token = :deviceId WHERE id = :uId")
    fun updateDeviceId(uId: Int = Volentiere_Primary_key, deviceId: String):Unit


    @Query("DELETE FROM `user_table` Where Uid = :id")
    fun logOut(id: Int = Volentiere_Primary_key)
}