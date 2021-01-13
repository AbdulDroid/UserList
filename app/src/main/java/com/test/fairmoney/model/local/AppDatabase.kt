package com.test.fairmoney.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.local.entities.Location
import com.test.fairmoney.model.local.entities.User

@Database(
    entities = [User::class, FullUser::class, Location::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}