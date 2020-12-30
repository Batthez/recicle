package com.matthbr.recicle.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.matthbr.recicle.R
import com.matthbr.recicle.domain.model.Item
import com.matthbr.recicle.mvi.MVI
import com.matthbr.recicle.view.fragment.listeners.OnItemClickListener

class ItemListAdapter(
  private val context: Context
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    private var itemList = mutableListOf<Item>()
    private var onItemClickListener : OnItemClickListener? = null

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
        holder.layout.setOnClickListener {
            onItemClickListener?.onItemClick(currentItem.id)
        }
    }

    fun setOnItemClick(onClickListener : OnItemClickListener){
        onItemClickListener = onClickListener
    }

    fun setItems(items : List<Item>) {
        this.itemList = items.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val description : TextView = itemView.findViewById(R.id.text_descricao_item)
        val quantity : TextView = itemView.findViewById(R.id.text_quantidade_item)
        val layout : ConstraintLayout = itemView.findViewById(R.id.constraint_layout_item)
    }
}