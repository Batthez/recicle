package com.matthbr.recicle.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matthbr.recicle.R
import com.matthbr.recicle.domain.model.Item
import com.matthbr.recicle.mvi.MVI
import com.matthbr.recicle.view.adapter.ItemListAdapter
import com.matthbr.recicle.view.fragment.listeners.OnItemClickListener
import com.matthbr.recicle.view.store.ItemStore
import com.matthbr.recicle.view.store.StoreFactory
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.InternalCoroutinesApi

class ItemListFragment : Fragment() {

    private lateinit var store: ItemStore
    private lateinit var storeFactory: StoreFactory
    private lateinit var itemListAdapter: ItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =  inflater.inflate(R.layout.fragment_first, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStore()
        initData()
        setupItemList()
        settingOnItemClick()

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun initStore() {
        storeFactory = StoreFactory(requireContext())

        store = ViewModelProvider(viewModelStore, storeFactory).get(ItemStore::class.java)
        store.state.observe(viewLifecycleOwner, stateObserver())
    }

    private fun initData(){
        store.actionLoadItems()
    }

    private fun stateObserver() = Observer<MVI.Store.State<ItemStore.Data>> { state ->
        with(state.data) {

            if(itemList == null){
                Log.e("Fragment","SEM DADOS")
                return@with
            }

            itemList?.let {
                setUpAdapterList(it)
            }

        }
    }

    private fun settingOnItemClick(){
        itemListAdapter.setOnItemClick(object : OnItemClickListener{
            override fun onItemClick(itemId: Int) {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundleOf("itemId" to itemId))
            }
        })
    }

    private fun setUpAdapterList(item : List<Item>){
        itemListAdapter.setItems(item)
    }

    private fun setupItemList(){
        itemListAdapter = ItemListAdapter(requireContext())
        val itemListRecyclerView = recycler_item_list
        itemListRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))
        itemListRecyclerView.adapter = itemListAdapter
    }

}