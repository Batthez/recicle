package com.matthbr.recicle.view.store

import com.matthbr.recicle.data.repository.ItemListRepositoryImpl
import com.matthbr.recicle.domain.model.Item
import com.matthbr.recicle.mvi.MVI

class ItemStore(
    private val itemRepository: ItemListRepositoryImpl
) : MVI.Store<ItemStore.Data, ItemStore.Intent>() {

    data class Data(
        var itemList: List<Item>? = null,
    )

    sealed class Intent : MVI.Store.Intent() {
        class LoadAllItems : Intent()
        class InsertNewItem(
            val description: String,
            val quantity: Int
        ) : Intent()
    }

    override fun initialData(): Data = Data()

    override suspend fun resolveIntent(intent: Intent) =
        when (intent) {
            is Intent.LoadAllItems -> reducerLoadCustomerWithDetails(intent)
            is Intent.InsertNewItem -> reducerInsertNewItem(intent)
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


}