package com.matthbr.recicle.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matthbr.recicle.R
import com.matthbr.recicle.domain.model.Item

class ItemListAdapter(
  private val context: Context
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    private var itemList = mutableListOf<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list, parent,false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.description.text = currentItem.description
        holder.quantity.text = currentItem.quantity.toString()
    }

    fun setItems(items : List<Item>) {
        this.itemList = items.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var description : TextView = itemView.findViewById(R.id.text_descricao_item)
        var quantity : TextView = itemView.findViewById(R.id.text_quantidade_item)
    }
}