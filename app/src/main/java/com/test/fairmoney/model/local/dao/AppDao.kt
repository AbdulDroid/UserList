package com.test.fairmoney.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.local.entities.User

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(users: List<User>)

    @Query("select * from user order by userId asc")
    fun getUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFullUser(user: FullUser)

    @Query("select * from fulluser where userId=:userId")
    fun getUser(userId: String): FullUser?
}