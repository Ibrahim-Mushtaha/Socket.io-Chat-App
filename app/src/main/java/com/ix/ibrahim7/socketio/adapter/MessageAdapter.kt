package com.ix.ibrahim7.socketio.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.model.Message
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
        var activity: Activity, var data: MutableList<Message>, val itemclick: onClick
) :
        RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var userid=getUser(activity).id

    class MessageViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun getItemViewType(position: Int): Int {
        when (data[position].type) {
            TEXT -> {
                if (data[position].SenderName == userid) {
                    return R.layout.item_sender_message
                } else {
                    return R.layout.item_reception_message
                }
            }
            else -> {
                if (data[position].SenderName == userid) {
                    return R.layout.item_sender_image
                } else {
                    return R.layout.item_reception_image
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(viewType, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        holder.itemView.apply {
            when (data[position].type) {
                TEXT -> {
                    if (data[position].SenderName == userid) {
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
                    if (data[position].SenderName == userid) {
                        txt_date_image.text =
                                android.text.format.DateFormat.format("hh:mm a", data[position].date)

                        tvimage_sender.setImageBitmap(decodeImage(data[position].message))

                        tvimage_sender.setOnClickListener {
                            itemclick.onClickItem(holder.adapterPosition, 1)
                        }


                    } else {
                        txt_date_image_recive.text =
                                android.text.format.DateFormat.format("hh:mm a", data[position].date)
                        tvimage_recive.setImageBitmap(decodeImage(data[position].message))

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
