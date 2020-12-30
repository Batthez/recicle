package com.matthbr.recicle.data.repository

import com.matthbr.recicle.data.database.ItemDAO
import com.matthbr.recicle.domain.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ItemListRepository{
    suspend fun getAllItems() : List<Item>
    suspend fun insertNewItem(item : Item)
    suspend fun updateItem(item: Item)
    suspend fun deleteItem(item: Item)
    suspend fun fetchItemById(itemId : Int) : Item
}

class ItemListRepositoryImpl(
        private val itemDAO: ItemDAO
) : ItemListRepository{
    override suspend fun getAllItems(): List<Item> = withContext(Dispatchers.IO){
        itemDAO.getAllItems()
    }

    override suspend fun insertNewItem(item : Item) = withContext(Dispatchers.IO){
        itemDAO.insertNewItem(item)
    }

    override suspend fun updateItem(item: Item) {
        itemDAO.updateItem(item)
    }

    override suspend fun deleteItem(item: Item) {
        itemDAO.deleteItem(item)
    }

    override suspend fun fetchItemById(itemId: Int) : Item = withContext(Dispatchers.IO) {
        itemDAO.getItemById(itemId)
    }

}