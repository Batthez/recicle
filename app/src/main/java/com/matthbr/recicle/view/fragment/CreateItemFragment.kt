package com.matthbr.recicle.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.matthbr.recicle.R
import com.matthbr.recicle.mvi.MVI
import com.matthbr.recicle.view.store.ItemStore
import com.matthbr.recicle.view.store.StoreFactory
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.coroutines.InternalCoroutinesApi

class CreateItemFragment : Fragment() {

    private lateinit var store : ItemStore
    private lateinit var storeFactory: StoreFactory

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
        = inflater.inflate(R.layout.fragment_second, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStore()
        button_second.setOnClickListener{
            insertNewItem()
        }
    }

    private fun fieldsOk() : Boolean{
        val description = text_descricao.text.toString()
        val quantity = text_quantidade.text.toString()
        return !description.trim().isEmpty() && !quantity.trim().isEmpty()
    }

    private fun initStore(){
        storeFactory = StoreFactory(requireContext())
        store = ViewModelProvider(viewModelStore,storeFactory).get(ItemStore::class.java)
        store.state.observe(viewLifecycleOwner, stateObserver())
    }

    private fun handleLoadingIndicator(loading : Boolean){
        button_second.isClickable = !loading
    }

    private fun insertNewItem(){
        if(fieldsOk()){
            val description = text_descricao.text.toString().trim()
            val quantity = text_quantidade.text.toString().toInt()
            store.actionInsertItem(description,quantity)
        }else{
            Toast.makeText(requireContext(),getString(R.string.preencha_os_campos), Toast.LENGTH_LONG).show()
        }
    }

    private fun stateObserver() = Observer<MVI.Store.State<ItemStore.Data>> { state ->
        with(state.data){
            handleLoadingIndicator(state.loading)

            state.message?.let {
                if(it.type == MVI.Store.MessageType.SUCCESS){
                    dialog(it.message!!)
                }

            }
        }
    }

    private fun dialog(text : String){
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(text)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            findNavController().navigateUp()
        }
        dialogBuilder.show()
    }

    private fun clearFields(){

    }
}