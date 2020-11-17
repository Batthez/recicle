package com.matthbr.recicle.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matthbr.recicle.domain.model.Item


@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Item::class
    ]
)

abstract class RecycleDatabase : RoomDatabase(){
    abstract fun itemDao(): ItemDAO
}