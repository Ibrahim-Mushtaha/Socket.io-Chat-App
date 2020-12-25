package com.ix.ibrahim7.socketio.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.databinding.ItemSelectUserBinding
import com.ix.ibrahim7.socketio.databinding.ItemUserBinding
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.decodeImage
import kotlinx.android.synthetic.main.item_select_user.view.*
import kotlinx.android.synthetic.main.item_user.view.*


class UserAdapter(val activity: Activity,
                  var data: ArrayList<User>, val itemclick: onClick, val type: Int
) :
        RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    class UserViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        when (type) {
            1 -> {
                val itemView_layout: ItemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.item_user, parent, false)
                return UserViewHolder(itemView_layout.root)
            }
            else -> {
                val itemView_layout: ItemSelectUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.item_select_user, parent, false)
                return UserViewHolder(itemView_layout.root)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {


        val currentItem = data[position]
        holder.itemView.apply {

            when (type) {
                1 -> {
                    txt_name.text = currentItem.username
                    if (!currentItem.isOnline) tvOnlineStatus.visibility = View.INVISIBLE
                    if (currentItem.image != "") tvProfileImage.setImageBitmap(decodeImage(currentItem.image))
                }
                else -> {
                    checkBox.text = data[position].username
                    if (currentItem.image != "") tv_profile_image.setImageBitmap(decodeImage(currentItem.image))
                    checkBox.setOnClickListener {
                        if (checkBox.isChecked) {
                            itemclick.onClickItem(currentItem, holder.adapterPosition, 1)
                        } else {
                            itemclick.onClickItem(currentItem, holder.adapterPosition, 2)
                            checkBox.isChecked = false
                        }
                    }
                }
            }
            setOnClickListener {
                itemclick.onClickItem(currentItem, holder.adapterPosition, 1)
            }


        }


    }

    interface onClick {
        fun onClickItem(user: User, position: Int, type: Int)
    }


}
