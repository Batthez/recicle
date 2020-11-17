package com.matthbr.recicle.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var INSTANCE : RecycleDatabase? = null

        fun getDatabase(context : Context) : RecycleDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext, RecycleDatabase::class.java,
                        "recycle_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}