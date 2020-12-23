package com.ix.ibrahim7.socketio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ix.ibrahim7.socketio.adapter.UserAdapter
import com.ix.ibrahim7.socketio.adapter.ViewPagerLogin
import com.ix.ibrahim7.socketio.databinding.FragmentMainBinding
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.ui.fragment.dialog.AddGroupFragment
import com.ix.ibrahim7.socketio.ui.fragment.home.AllGroupFragment
import com.ix.ibrahim7.socketio.ui.fragment.home.HomeFragment
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.ALLUSERS
import com.ix.ibrahim7.socketio.util.Constant.TAG
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.ix.ibrahim7.socketio.util.Constant.getSharePref
import com.ix.ibrahim7.socketio.util.Constant.getUser
import devjdelasen.com.sidebubbles.SideBubbles
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type


class MainFragment : Fragment() {

    private val viewAdapter by lazy {
        ViewPagerLogin(childFragmentManager)
    }

    private lateinit var mBinding: FragmentMainBinding
    var array = ArrayList<User>()

    private var mSocket: Socket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().tabs.visibility=View.VISIBLE
        setHasOptionsMenu(true)
        mBinding = FragmentMainBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ChatApplication().apply {
            getEmitterListener(Socket.EVENT_CONNECT_ERROR, onConnectError)
            getEmitterListener(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            getEmitterListener(Socket.EVENT_CONNECT, onConnect)
            getEmitterListener(Socket.EVENT_DISCONNECT, onDisconnect)
            getEmitterListener(ALLUSERS, AllUser)
            mSocket=getSocket()
            getSocket()!!.connect()
        }

        setUpViewPager()


        setUpSideBubbles(array)


        super.onViewCreated(view, savedInstanceState)
    }

    var onConnect = Emitter.Listener {
        sendUserJoin()
        Log.v("eee", "Socket Connected!")
    }

    private val onConnectError = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            Log.v("eee", "Socket Connected!")
        }
    }

    private val onDisconnect = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            Log.v("eee", "Socket Connected!")
        }
    }


    private val AllUser = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            val mutableListType: Type = object : TypeToken<List<User>>() {}.type
            val users = Gson().fromJson<List<User>>(args[0].toString(), mutableListType)
            users.forEach {users->
                if (users.username != getUser(requireContext()).username) {
                    array.clear()
                    array.add(users) // id
                    array.distinct()
                    Log.v("$TAG User Array", array.toString())
                }
            }
        }
    }



    fun sendUserJoin() {
        try {
            val user = JSONObject().apply {
                put(Constant.NAME,getUser(requireContext()).username)
                put(Constant.ID,getUser(requireContext()).id)
            }
           mSocket!!.emit("join",user)
        } catch (e: JSONException) {
            Log.v("me", "error send message " + e.message)
        }
    }


    private fun setUpViewPager(){
        if (viewAdapter.lf.size == 0) {
            viewAdapter.addFragment(
                HomeFragment(),"Chat")
            viewAdapter.addFragment(
                AllGroupFragment(), "Groups")
        }
        mBinding.viewPager2.adapter = viewAdapter
        requireActivity().tabs.setupWithViewPager(mBinding.viewPager2)
    }


    private fun setUpSideBubbles(arrayList: ArrayList<User>){
        sideBubbles.addItem("addGroup", R.drawable.ic_group_add, ContextCompat.getColor(requireContext(), R.color.purple_700))
        sideBubbles.setClickItemListener(object : SideBubbles.OnClickItemListener{
            override fun onClickItem(id: String) {
                when(id){
                    "addGroup"->{
                        AddGroupFragment(arrayList).show(childFragmentManager,"")
                    }
                }
            }

        })
    }

}