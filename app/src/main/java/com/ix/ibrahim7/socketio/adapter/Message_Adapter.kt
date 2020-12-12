package com.ix.ibrahim7.socketio.adapter

import android.app.Activity
import android.util.Base64.DEFAULT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.nkzawa.utf8.UTF8.decode
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.model.TextMessage
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.IMAGE
import com.ix.ibrahim7.socketio.util.Constant.TEXT
import com.ix.ibrahim7.socketio.util.Constant.decodeImage
import kotlinx.android.synthetic.main.item_reception_image.view.*
import kotlinx.android.synthetic.main.item_reception_message.view.*
import kotlinx.android.synthetic.main.item_sender_image.view.*
import kotlinx.android.synthetic.main.item_sender_message.view.*
import java.lang.Byte.decode
import java.security.spec.PSSParameterSpec.DEFAULT
import java.util.*


class Message_Adapter(
        var activity: Activity, var data: MutableList<TextMessage>, val itemclick: onClick
) :
    RecyclerView.Adapter<Message_Adapter.MyViewHolder>() {


    val myName = Constant.getSharePref(activity).getString(Constant.USER,"")

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun getItemViewType(position: Int): Int {
        if (data[position].type == TEXT) {
            if (data[position].SenderName ==myName) {
                return R.layout.item_sender_message
            } else {
                return R.layout.item_reception_message
            }
        } else{
            if (data[position].SenderName == myName) {
                return R.layout.item_sender_image
            } else {
                return R.layout.item_reception_image
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(viewType, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.apply {
            if (data[position].type == TEXT) {
                if (data[position].SenderName == myName) {
                    txt_name.text = data[position].message
                    holder.itemView.txt_date.text =
                        android.text.format.DateFormat.format("hh:mm a", data[position].date)
                } else {
                    txt_name_receive.text = data[position].message
                    holder.itemView.txt_date_receive.text =
                        android.text.format.DateFormat.format("hh:mm a", data[position].date)
                }
            }  else if(data[position].type == IMAGE){
                if (data[position].SenderName == myName) {
                    txt_date_image.text =
                        android.text.format.DateFormat.format("hh:mm a", data[position].date)

                    Constant.getBitmapImage(activity,decodeImage(data as ArrayList<TextMessage>,position),holder.itemView.tvimage_sender)

                    tvimage_sender.setOnClickListener {
                        itemclick.onClickItem(holder.adapterPosition,1)
                    }



                } else{
                    txt_date_image_recive.text =
                        android.text.format.DateFormat.format("hh:mm a", data[position].date)

                    Constant.getBitmapImage(activity,decodeImage(data as ArrayList<TextMessage>,position),holder.itemView.tvimage_recive)

                    tvimage_recive.setOnClickListener {
                        itemclick.onClickItem(holder.adapterPosition,2)
                    }


                }
            }
        }

    }

    interface onClick {
        fun onClickItem(position: Int, type: Int)
    }




}
