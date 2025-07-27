package com.talha.shoppinglist.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.talha.shoppinglist.model.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}