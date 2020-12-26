package com.ix.ibrahim7.socketio.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.ViewPagerAdapter
import com.ix.ibrahim7.socketio.databinding.FragmentMainBinding
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.ui.fragment.dialog.AddGroupDialog
import com.ix.ibrahim7.socketio.ui.fragment.home.ListGroupFragment
import com.ix.ibrahim7.socketio.ui.fragment.home.ListUserFragment
import com.ix.ibrahim7.socketio.ui.viewmodel.HomeViewModel
import com.ix.ibrahim7.socketio.network.SocketManager
import com.ix.ibrahim7.socketio.util.Constant.ALLUSERS
import com.ix.ibrahim7.socketio.util.Constant.ID
import com.ix.ibrahim7.socketio.util.Constant.ISONLINE
import com.ix.ibrahim7.socketio.util.Constant.JOIN
import com.ix.ibrahim7.socketio.util.Constant.NAME
import com.ix.ibrahim7.socketio.util.Constant.TAG
import com.ix.ibrahim7.socketio.util.Constant.getUser
import com.ix.ibrahim7.socketio.util.Constant.removeDuplicates
import devjdelasen.com.sidebubbles.SideBubbles
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type


class MainFragment : Fragment() {

    private val viewAdapter by lazy {
        ViewPagerAdapter(childFragmentManager)
    }

    private lateinit var mBinding: FragmentMainBinding
    var array = ArrayList<User>()
    private var mSocket: Socket? = null


    private val viewModel by lazy {
    ViewModelProvider(this)[HomeViewModel::class.java]
    }


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

        SocketManager().apply {
            getEmitterListener(Socket.EVENT_CONNECT_ERROR, onConnectError)
            getEmitterListener(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            getEmitterListener(Socket.EVENT_CONNECT, onConnect)
            getEmitterListener(Socket.EVENT_DISCONNECT, onDisconnect)
            getEmitterListener(ALLUSERS, AllUser)
            mSocket=getSocket()
        }

        setUpViewPager()


        setUpSideBubbles(array)


        super.onViewCreated(view, savedInstanceState)
    }

    var onConnect = Emitter.Listener {
        joinUser()
        Log.v("eee", "Socket Connected!")
    }

    private val onConnectError = Emitter.Listener {}
    private val onDisconnect = Emitter.Listener {}


    private val AllUser = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            val mutableListType: Type = object : TypeToken<List<User>>() {}.type
            val users = Gson().fromJson<List<User>>(args[0].toString(), mutableListType)
            array.clear()
            users.forEach {users->
                if (users.username != getUser(requireContext()).username) {
                    array.add(0,users)
                    Log.v("$TAG User Array", array.toString())
                }
            }
            array = removeDuplicates(array)!!
            viewModel.getUsers(array)
        }
    }



    fun joinUser() {
        try {
            val user = JSONObject().apply {
                put(ID,getUser(requireContext()).id)
                put(NAME,getUser(requireContext()).username)
                put(ISONLINE,true)
            }
            mSocket!!.emit(JOIN,user)
        } catch (e: JSONException) {
            Log.v("me", "error send message " + e.message)
        }
    }

    private fun setUpViewPager(){
        if (viewAdapter.getLf().size == 0) {
            viewAdapter.addFragment(
                ListUserFragment(),"Chat")
            viewAdapter.addFragment(
                ListGroupFragment(), "Groups")
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
                        AddGroupDialog(arrayList).show(childFragmentManager,"")
                    }
                }
            }

        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.editProfile->{
                parentFragment?.findNavController()?.navigate(R.id.action_mainFragment_to_profileFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}