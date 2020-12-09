package com.ix.ibrahim7.socketio.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.ix.ibrahim7.socketio.adapter.Message_Adapter
import com.ix.ibrahim7.socketio.databinding.FragmentChatBinding
import com.ix.ibrahim7.socketio.model.TextMessage
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.USER
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment(), Message_Adapter.onClick {


    private lateinit var mBinding: FragmentChatBinding

    private var mSocket: Socket? = null


    private val adapter by lazy {
        Message_Adapter(requireActivity(), ArrayList(), this)
    }

    private val arg by lazy {
        requireArguments().getString("Des_id")
    }

    val des_id= 1


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        mBinding = FragmentChatBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar.title =arg
        chat_list.adapter = adapter

        val app = ChatApplication()
        mSocket = app.socket
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
       // mSocket!!.on("msgS", onNewMessage)
        mSocket!!.on("message", onNewMessage)
       // mSocket!!.on("join", AllUser)
        mSocket!!.connect()






        btn_send.setOnClickListener {
            attemptSend()
        }


    }


    override fun onClickItem(position: Int, type: Int) {
        when (type) {
            1 -> {
                //  ShowImageFragment(array[position].message).show(childFragmentManager,"")
            }
            2 -> {
                // ShowImageFragment(array[position].message).show(childFragmentManager,"")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {

        }
    }


    var onConnect = Emitter.Listener {
        Log.e("eee", "Socket Connected!")
    }

    private val onConnectError = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!") } }
    private val onDisconnect = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!") } }


 /*   private val onNewMessage = Emitter.Listener { args ->
        requireActivity().runOnUiThread(Runnable {
            val data =args[0] as String
            try {
                val msgs = data.split(":".toRegex()).toTypedArray()
                val name = msgs[0]
                val message = msgs[1]
                val massage = TextMessage()
                massage.SenderName = msgs[0]
                massage.message = msgs[1]
                adapter.data.add(TextMessage(message,name, Calendar.getInstance().time, Constant.TEXT))
                adapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                return@Runnable
            }

        })
    }*/



    private val onNewMessage = Emitter.Listener { args ->
        requireActivity().runOnUiThread(Runnable {
            val data =args[0] as JSONObject
            try {

             // if (data.getString("des_id").equals(arg) || data.getString("source_id").equals(Constant.getSharePref(requireContext()).getString(Constant.USERID,""))){
                    adapter.data.add(TextMessage(data.getString("message"), data.getString("source_id"), Calendar.getInstance().time, Constant.TEXT))
                    adapter.notifyDataSetChanged()
                Log.e("ttt message ", data.toString())
                /*}else {
                    Log.e("ttt", data.getString("des_id"))
                }*/
            } catch (e: Exception) {
               Log.e("eee ex",e.message.toString())
            }

        })
    }

/*
    private val AllUser = Emitter.Listener { args ->
        requireActivity().runOnUiThread(Runnable {
            val data = args[0] as String
            // get the extra data from the fired event and display a toast
            Log.v("ttt", data)
        })
    }
*/




    private fun attemptSend() {
        val message = JSONObject().apply {
            put("message",etxt_massege.text.toString())
            put("source_id",Constant.getSharePref(requireContext()).getString(Constant.USER,""))
            put("des_id",arg)
        }
        //mSocket!!.emit("msgS", "${Constant.getSharePref(requireContext()).getString(USER, "ibra")}" + ":" + etxt_massege.text.toString())
        mSocket!!.emit("message", message)
        etxt_massege.setText("")
    }




     fun attemptSend2() {
        try {
            mSocket!!.emit("join", Constant.getSharePref(requireContext()).getString(USER, "ibar"))
        } catch (e: JSONException) {
            Log.d("me", "error send message " + e.message)
        }
        // mSocket!!.emit("msgS", message)
    }

}


