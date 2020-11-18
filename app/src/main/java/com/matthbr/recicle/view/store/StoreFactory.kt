package com.matthbr.recicle.view.store

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matthbr.recicle.data.database.RecycleDatabase
import com.matthbr.recicle.data.repository.ItemListRepositoryImpl
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.IllegalArgumentException

class StoreFactory(private val context: Context) : ViewModelProvider.Factory{
    @InternalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ItemStore::class.java)){
            return ItemStore(
                    ItemListRepositoryImpl(
                            RecycleDatabase.getDatabase(context).itemDao()
                    )
            ) as T
        }

        throw IllegalArgumentException("ViewModel not configured")
    }

}