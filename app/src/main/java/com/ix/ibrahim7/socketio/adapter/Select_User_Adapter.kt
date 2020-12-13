package com.ix.ibrahim7.socketio.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.databinding.ItemUserSelectGroupBinding
import com.ix.ibrahim7.socketio.model.User


class Select_User_Adapter(val activity: Activity,
                          var data: MutableList<User>, val itemclick: onClickListener
) :
    RecyclerView.Adapter<Select_User_Adapter.MyViewHolder>() {


    class MyViewHolder(val item: ItemUserSelectGroupBinding) : RecyclerView.ViewHolder(item.root) {


        val tv_check = item.checkBox
        fun bind(n: User) {
            item.checkBoxUser = n
            item.executePendingBindings()
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemUserSelectGroupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_user__select_group, parent, false
        )
        return MyViewHolder(itemView_layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setUsers(lsit: List<User>) {
        this.data = lsit as MutableList<User>
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tv_check.text = data[position].username

        holder.tv_check.setOnClickListener {
            if (holder.tv_check.isChecked) {
                itemclick.onClickItem(holder.adapterPosition, 1)
            }else{
                itemclick.onClickItem(holder.adapterPosition, 2)
                holder.tv_check.isChecked=false
            }
        }
       // Glide.with(activity).load(data[position].PorfileImage).placeholder(R.drawable.ic_profile_img).error(R.drawable.ic_profile_img).into(holder.item.tvCircleImageView)
        val currentItem = data[position]
        holder.bind(currentItem)


    }

    interface onClickListener {
        fun onClickItem(position: Int, type: Int)
    }





}
