package com.matthbr.recicle.view.store

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matthbr.recicle.data.database.RecycleDatabase
import com.matthbr.recicle.data.repository.ItemListRepositoryImpl

class StoreFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemDetailsStore(
                ItemListRepositoryImpl(
                        RecycleDatabase.getDatabase(context).itemDao()
                )
        ) as T
    }

}