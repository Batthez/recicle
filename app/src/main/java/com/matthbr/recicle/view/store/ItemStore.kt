package com.matthbr.recicle.view.store

import com.matthbr.recicle.data.repository.ItemListRepositoryImpl
import com.matthbr.recicle.domain.model.Item
import com.matthbr.recicle.mvi.MVI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ItemStore(
    private val itemListRepository  : ItemListRepositoryImpl
) : MVI.Store<ItemStore.Data, ItemStore.Intent>() {

    data class Data(
        var itemList : List<Item> ? = null,
    )

    @ExperimentalCoroutinesApi
    sealed class Intent : MVI.Store.Intent(){
        class LoadAllItems : Intent()
    }

    override fun initialData(): Data = Data()

    override suspend fun resolveIntent(intent: Intent) =
        when(intent){
            is Intent.LoadAllItems -> reducerLoadCustomerWithDetails(intent)
        }


     private suspend fun reducerLoadCustomerWithDetails(intent: Intent.LoadAllItems) =
        produceReducer { setState ->

            val itemList = itemListRepository.getAllItems()

            setState(
                getState().copy(
                    data = getState().data.copy(
                        itemList = itemList
                    )
                )
            )
        }

    fun actionLoadItems() = produceAction { dispatch ->
        dispatch(MVI.Store.Intent.LoadingIntent(loading = true))
        dispatch(Intent.LoadAllItems())
        dispatch(MVI.Store.Intent.LoadingIntent(loading = false))
    }
}