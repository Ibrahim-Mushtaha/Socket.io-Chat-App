package com.ix.ibrahim7.socketio.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.model.TextMessage
import com.ix.ibrahim7.socketio.util.Constant.TEXT
import com.ix.ibrahim7.socketio.util.Constant.decodeImage
import com.ix.ibrahim7.socketio.util.Constant.getBitmapImage
import com.ix.ibrahim7.socketio.util.Constant.getUser
import kotlinx.android.synthetic.main.item_reception_image.view.*
import kotlinx.android.synthetic.main.item_reception_message.view.*
import kotlinx.android.synthetic.main.item_sender_image.view.*
import kotlinx.android.synthetic.main.item_sender_message.view.*
import java.util.*


class MessageAdapter(
        var activity: Activity, var data: MutableList<TextMessage>, val itemclick: onClick
) :
        RecyclerView.Adapter<MessageAdapter.ViewHolder>() {


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun getItemViewType(position: Int): Int {
        when (data[position].type) {
            TEXT -> {
                if (data[position].SenderName == getUser(activity).id) {
                    return R.layout.item_sender_message
                } else {
                    return R.layout.item_reception_message
                }
            }
            else -> {
                if (data[position].SenderName == getUser(activity).id) {
                    return R.layout.item_sender_image
                } else {
                    return R.layout.item_reception_image
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(viewType, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.apply {
            when (data[position].type) {
                TEXT -> {
                    if (data[position].SenderName == getUser(activity).id) {
                        txt_name.text = data[position].message
                        holder.itemView.txt_date.text =
                                android.text.format.DateFormat.format("hh:mm a", data[position].date)
                    } else {
                        txt_name_receive.text = data[position].message
                        holder.itemView.txt_date_receive.text =
                                android.text.format.DateFormat.format("hh:mm a", data[position].date)
                    }
                }
                else -> {
                    if (data[position].SenderName == getUser(activity).id) {
                        txt_date_image.text =
                                android.text.format.DateFormat.format("hh:mm a", data[position].date)

                        getBitmapImage(activity, decodeImage(data as ArrayList<TextMessage>, position), holder.itemView.tvimage_sender)

                        tvimage_sender.setOnClickListener {
                            itemclick.onClickItem(holder.adapterPosition, 1)
                        }


                    } else {
                        txt_date_image_recive.text =
                                android.text.format.DateFormat.format("hh:mm a", data[position].date)

                        getBitmapImage(activity, decodeImage(data as ArrayList<TextMessage>, position), holder.itemView.tvimage_recive)

                        tvimage_recive.setOnClickListener {
                            itemclick.onClickItem(holder.adapterPosition, 2)
                        }


                    }
                }
            }
        }

    }

    interface onClick {
        fun onClickItem(position: Int, type: Int)
    }


}