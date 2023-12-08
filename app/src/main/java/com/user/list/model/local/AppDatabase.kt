package com.user.list.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.user.list.model.local.dao.AppDao
import com.user.list.model.local.entities.FullUser
import com.user.list.model.local.entities.Location
import com.user.list.model.local.entities.User

@Database(
    entities = [User::class, FullUser::class, Location::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}