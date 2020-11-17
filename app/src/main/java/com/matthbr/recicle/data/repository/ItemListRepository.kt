package com.matthbr.recicle.data.repository

import com.matthbr.recicle.data.database.ItemDAO
import com.matthbr.recicle.domain.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ItemListRepository{
    suspend fun getAllItems() : List<Item>
}

class ItemListRepositoryImpl(
        private val itemDAO: ItemDAO
) : ItemListRepository{
    override suspend fun getAllItems(): List<Item> = withContext(Dispatchers.IO){
        itemDAO.getAllItems()
    }

}