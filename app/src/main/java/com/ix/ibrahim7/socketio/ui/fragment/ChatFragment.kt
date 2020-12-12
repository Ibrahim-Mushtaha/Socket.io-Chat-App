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
import com.ix.ibrahim7.socketio.util.Constant.MESSAGE
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
        mSocket!!.on(MESSAGE, onNewMessage)
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



    var onConnect = Emitter.Listener {
        Log.e("eee", "Socket Connected!")
    }

    private val onConnectError = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!") } }
    private val onDisconnect = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!") } }





    private val onNewMessage = Emitter.Listener { args ->
        requireActivity().runOnUiThread(Runnable {
            val data =args[0] as JSONObject
            try {

              if (data.getString("des_id").equals(Constant.getSharePref(requireContext()).getString(USER,"")) && data.getString("source_id").equals(arg)){
                    adapter.data.add(TextMessage(data.getString("message"), data.getString("source_id"), Calendar.getInstance().time, Constant.TEXT))
                    adapter.notifyDataSetChanged()
                Log.e("ttt message ", data.toString())
                }else {
                    Log.e("ttt", data.getString("des_id"))
                }
            } catch (e: Exception) {
               Log.e("eee ex",e.message.toString())
            }

        })
    }




    private fun attemptSend() {
        val message = JSONObject().apply {
            put("message",etxt_massege.text.toString())
            put("source_id",Constant.getSharePref(requireContext()).getString(USER,""))
            put("des_id",arg)
        }
        adapter.data.add(TextMessage(etxt_massege.text.toString(),Constant.getSharePref(requireContext()).getString(USER,"").toString(), Calendar.getInstance().time, Constant.TEXT))
        adapter.notifyDataSetChanged()
        mSocket!!.emit("message", message)
        etxt_massege.setText("")
    }





}


