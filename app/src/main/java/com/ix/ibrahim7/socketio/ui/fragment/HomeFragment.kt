package com.ix.ibrahim7.socketio.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.UserAdapter
import com.ix.ibrahim7.socketio.databinding.FragmentHomeBinding
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException



class HomeFragment : Fragment(), UserAdapter.onClick {

    lateinit var mdialog: Dialog


    private lateinit var mBinding: FragmentHomeBinding

    var array = ArrayList<User>()

    private val adapter by lazy {
        UserAdapter(requireActivity(),array, this)
    }

    private var mSocket: Socket? = null

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val app = ChatApplication()
        mSocket = app.getSocket()
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        // mSocket!!.on("msgS", onNewMessage)
        mSocket!!.on("join", AllUser)
        mSocket!!.connect()


        list_item.adapter = adapter
        list_item.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.recyclerview_layout_animation)



        super.onViewCreated(view, savedInstanceState)
    }

    var onConnect = Emitter.Listener {
        attemptSend2()
        Log.e("eee", "Socket Connected!")
    }

    private val onConnectError = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!") } }
    private val onDisconnect = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!") } }


    private val AllUser = Emitter.Listener { args ->
        requireActivity().runOnUiThread(Runnable {
            val data = args[0] as String
            if (data !=Constant.getSharePref(requireContext()).getString(Constant.USER, "ibar")) {
                adapter.data.add(0, User(data))
                adapter.data.distinct()
                Log.e("eeee array", adapter.data.toString())
                adapter.notifyDataSetChanged()
            }
            // get the extra data from the fired event and display a toast
            Log.v("ttt", data)
        })
    }

    fun attemptSend2() {
        try {
            mSocket!!.emit("join", Constant.getSharePref(requireContext()).getString(Constant.USER, "ibar"))
        } catch (e: JSONException) {
            Log.d("me", "error send message " + e.message)
        }
        // mSocket!!.emit("msgS", message)
    }


    override fun onClickItem(user: User,position: Int, type: Int) {
        when (type) {
            1 -> {
                val bundle = Bundle().apply {
                    putString("Des_id",user.username)
                }
                    findNavController().navigate(R.id.action_homeFragment_to_chatFragment,bundle)
            }
        }
    }



}
