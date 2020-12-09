package com.ix.ibrahim7.socketio.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.databinding.ItemUserBinding
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.util.Constant
import kotlinx.android.synthetic.main.item_user.view.*


class UserAdapter(val activity: Activity,
    var data: MutableList<User>, val itemclick: onClick
) :
        RecyclerView.Adapter<UserAdapter.MyViewHolder>() {


    class MyViewHolder(val item: ItemUserBinding) : RecyclerView.ViewHolder(item.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_user, parent, false)
        return MyViewHolder(itemView_layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val currentItem = data[position]
        holder.item.apply {
            allcard.setOnClickListener {
                itemclick.onClickItem(currentItem,holder.adapterPosition, 1)
            }

            holder.itemView.apply {
                txt_name.text =data[position].username
            }

        }



    }

    interface onClick {
        fun onClickItem(user: User,position: Int, type: Int)
    }




}
