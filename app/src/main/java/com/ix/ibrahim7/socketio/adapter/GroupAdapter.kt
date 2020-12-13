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
    RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {


    class MyViewHolder(val item: ItemGroupBinding) : RecyclerView.ViewHolder(item.root) {


        val tvname = item.txtGroupName
        val tvAll = item.allcard

        fun bind(n: Groups) {
            item.group = n
            item.executePendingBindings()
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemGroupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_group, parent, false
        )
        return MyViewHolder(itemView_layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvname.text=data[position].Group_name

        holder.tvAll.setOnClickListener {
            itemclick.onClickItem(holder.adapterPosition,1)
        }
        val currentItem = data[position]
        holder.bind(currentItem)


    }

    interface onClick {
        fun onClickItem(position: Int, type: Int)
    }




}
