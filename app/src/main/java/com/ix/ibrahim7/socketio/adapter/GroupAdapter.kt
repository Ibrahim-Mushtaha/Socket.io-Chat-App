package com.ix.ibrahim7.socketio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.databinding.ItemGroupBinding
import com.ix.ibrahim7.socketio.model.Groups


class GroupAdapter(
        var data: MutableList<Groups>, val itemclick: onClick
) :
        RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {


    class GroupViewHolder(val item: ItemGroupBinding) : RecyclerView.ViewHolder(item.root) {

        fun bind(n: Groups) {
            item.group = n
            item.executePendingBindings()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView_layout: ItemGroupBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_group, parent, false
        )
        return GroupViewHolder(itemView_layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {

        holder.itemView.apply {
            setOnClickListener {
                itemclick.onClickItem(data[position],holder.adapterPosition, 1)
            }
        }
        val currentItem = data[position]
        holder.bind(currentItem)


    }

    interface onClick {
        fun onClickItem(group: Groups,position: Int, type: Int)
    }


}
