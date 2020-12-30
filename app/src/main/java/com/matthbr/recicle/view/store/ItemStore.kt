package com.matthbr.recicle.view.store

import com.matthbr.recicle.data.repository.ItemListRepositoryImpl
import com.matthbr.recicle.domain.model.Item
import com.matthbr.recicle.mvi.MVI

class ItemStore(
    private val itemRepository: ItemListRepositoryImpl
) : MVI.Store<ItemStore.Data, ItemStore.Intent>() {

    data class Data(
        var itemList: List<Item>? = null,
        var actualItem : Item? = null
    )

    sealed class Intent : MVI.Store.Intent() {
        class LoadAllItems : Intent()
        class InsertNewItem(
            val description: String,
            val quantity: Int
        ) : Intent()
        class DeleteItem(
            val itemToDelete : Item
        ) : Intent()
        class UpdateItem(
            val itemId: Int,
            val description: String,
            val quantity: Int
        ) : Intent()
        class FetchItemById(
            val itemId: Int
        ) : Intent()
    }

    override fun initialData(): Data = Data()

    override suspend fun resolveIntent(intent: Intent) =
        when (intent) {
            is Intent.LoadAllItems -> reducerLoadCustomerWithDetails(intent)
            is Intent.InsertNewItem -> reducerInsertNewItem(intent)
            is Intent.UpdateItem -> reducerUpdateItem(intent)
            is Intent.DeleteItem -> reducerDeleteItem(intent)
            is Intent.FetchItemById -> reducerFetchItemById(intent)
        }


    private suspend fun reducerLoadCustomerWithDetails(intent: Intent.LoadAllItems) =
        produceReducer { setState ->
            val itemList = itemRepository.getAllItems()

            setState(
                getState().copy(
                    data = getState().data.copy(
                        itemList = itemList
                    )
                )
            )
        }

    private suspend fun reducerInsertNewItem(intent: Intent.InsertNewItem) =
        produceReducer { setState ->
            itemRepository.insertNewItem(
                Item(
                    0,
                    intent.description,
                    intent.quantity
                )
            )

            setState(
                getState().copy(
                    message = Message(MessageType.SUCCESS, null, "Item cadastrado!")
                )
            )

        }

    private suspend fun reducerUpdateItem(intent : Intent.UpdateItem) = produceReducer {setState ->
        itemRepository.updateItem(Item(
            id = intent.itemId,
            description = intent.description,
            quantity = intent.quantity
        ))
        setState(
            getState().copy(
                message = Message(MessageType.SUCCESS, null, "Item atualizado!")
            )
        )
    }

    private suspend fun reducerDeleteItem(intent : Intent.DeleteItem) = produceReducer { setState ->
        itemRepository.deleteItem(intent.itemToDelete)
    }

    private suspend fun reducerFetchItemById(intent : Intent.FetchItemById) = produceReducer { setState ->
        val itemFetched = itemRepository.fetchItemById(intent.itemId)
    }

    fun actionLoadItems() = produceAction { dispatch ->
        dispatch(MVI.Store.Intent.LoadingIntent(loading = true))
        dispatch(Intent.LoadAllItems())
        dispatch(MVI.Store.Intent.LoadingIntent(loading = false))
    }

    fun actionInsertItem(description: String, quantity: Int) = produceAction { dispatch ->
        dispatch(MVI.Store.Intent.LoadingIntent(loading = true))
        dispatch(Intent.InsertNewItem(description, quantity))
        dispatch(MVI.Store.Intent.LoadingIntent(loading = false))
    }

    fun actionClearMessage() = produceAction { dispatch ->
        dispatch(MVI.Store.Intent.MessageIntent(null))
    }

    fun actionDeleteItem(itemToDelete : Item) = produceAction {dispatch ->
        dispatch(MVI.Store.Intent.LoadingIntent(loading = true))
        dispatch(Intent.DeleteItem(itemToDelete))
        dispatch(MVI.Store.Intent.LoadingIntent(loading = false))
    }

    fun actionUpdateItem(itemId : Int, description: String, quantity: Int) = produceAction { dispatch ->
        dispatch(MVI.Store.Intent.LoadingIntent(loading = true))
        dispatch(Intent.UpdateItem(itemId,description,quantity))
        dispatch(MVI.Store.Intent.LoadingIntent(loading = false))
    }

    fun actionFetchItemById(itemId: Int) = produceAction { dispatch ->
        dispatch(MVI.Store.Intent.LoadingIntent(loading = true))
        dispatch(Intent.FetchItemById(itemId))
        dispatch(MVI.Store.Intent.LoadingIntent(loading = false))
    }


}