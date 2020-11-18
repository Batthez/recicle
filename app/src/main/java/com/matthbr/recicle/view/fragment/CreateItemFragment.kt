package com.matthbr.recicle.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.matthbr.recicle.R
import com.matthbr.recicle.mvi.MVI
import com.matthbr.recicle.view.store.ItemStore
import com.matthbr.recicle.view.store.StoreFactory
import kotlinx.coroutines.InternalCoroutinesApi

class CreateItemFragment : Fragment() {

    @InternalCoroutinesApi
    private lateinit var store : ItemStore
    private lateinit var storeFactory: StoreFactory

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStore()
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            store.actionInsertItem("aaa",3)
        }
    }

    @InternalCoroutinesApi
    private fun initStore(){
        storeFactory = StoreFactory(requireContext())
        store = ViewModelProvider(viewModelStore,storeFactory).get(ItemStore::class.java)
        store.state.observe(viewLifecycleOwner, stateObserver())
    }


    @InternalCoroutinesApi
    private fun stateObserver() = Observer<MVI.Store.State<ItemStore.Data>> { state ->

    }
}