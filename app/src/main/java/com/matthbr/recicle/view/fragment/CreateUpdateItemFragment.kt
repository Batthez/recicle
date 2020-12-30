package com.matthbr.recicle.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.matthbr.recicle.R
import com.matthbr.recicle.mvi.MVI
import com.matthbr.recicle.view.store.ItemStore
import com.matthbr.recicle.view.store.StoreFactory
import kotlinx.android.synthetic.main.fragment_second.*

class CreateUpdateItemFragment : Fragment() {

    private lateinit var store: ItemStore
    private lateinit var storeFactory: StoreFactory
    private lateinit var informationDialog: AlertDialog
    private var isToUpdate = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_second, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStore()
        verifyIfNeedsToUpdateItem()
        button_second.setOnClickListener {
            onClickCreateUpdateButton()
        }
    }

    private fun configureFragmentToUpdateItem(){
        text_view_title.text = getString(R.string.atualize_informacoes)

    }

    private fun verifyIfNeedsToUpdateItem(){
        arguments?.let {
            val itemId = it.get("itemId")
            if(itemId != null){
                configureFragmentToUpdateItem()
            }
        }
    }

    private fun fieldsOk(): Boolean {
        val description = text_descricao.text.toString()
        val quantity = text_quantidade.text.toString()
        return !description.trim().isEmpty() && !quantity.trim().isEmpty()
    }

    private fun initStore() {
        storeFactory = StoreFactory(requireContext())
        store = ViewModelProvider(viewModelStore, storeFactory).get(ItemStore::class.java)
        store.state.observe(viewLifecycleOwner, stateObserver())
    }

    private fun disableButtonClickability() {
        button_second.isClickable = false
    }

    private fun enableButtonClickability() {
        button_second.isClickable = true
    }



    private fun onClickCreateUpdateButton() {
        if (fieldsOk()) {
            val description = text_descricao.text.toString().trim()
            val quantity = text_quantidade.text.toString().toInt()

            if(isToUpdate){
                store.actionUpdateItem(id, description,quantity)
            }else{
                store.actionInsertItem(description, quantity)
            }
            disableButtonClickability()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.preencha_os_campos),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun stateObserver() = Observer<MVI.Store.State<ItemStore.Data>> { state ->
        with(state.data) {
            if (state.message?.type == MVI.Store.MessageType.SUCCESS) {
                state.message.message?.let {
                    dialog(it)
                }
                enableButtonClickability()
            }
        }
    }

    private fun dialog(text: String) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setTitle(text)
            setCancelable(false)
            setPositiveButton(getString(R.string.ok)) { _, _ -> clearFields() }
        }

        informationDialog = builder.create()
        informationDialog.show()

        store.actionClearMessage()
    }

    private fun clearFields() {
        text_quantidade.setText("")
        text_descricao.setText("")
        findNavController().navigate(R.id.action_close_fragment_and_navigate_to_listFragment)
    }
}