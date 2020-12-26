package com.ix.ibrahim7.socketio.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.UserAdapter
import com.ix.ibrahim7.socketio.databinding.FragmentListUserBinding
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.ui.viewmodel.HomeViewModel
import com.ix.ibrahim7.socketio.network.SocketManager
import com.ix.ibrahim7.socketio.util.Constant.ALLUSERS
import com.ix.ibrahim7.socketio.util.Constant.TAG
import com.ix.ibrahim7.socketio.util.Constant.TYPE
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.ix.ibrahim7.socketio.util.Constant.getUser
import com.ix.ibrahim7.socketio.util.Constant.removeDuplicates
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class ListUserFragment : Fragment(), UserAdapter.onClick {

    private lateinit var mBinding: FragmentListUserBinding

    private val user_Adapter by lazy {
        UserAdapter(requireActivity(),ArrayList(), this,1)
    }

    private var mSocket: Socket? = null
    private var showData = false

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().tabs.visibility=View.VISIBLE
        mBinding = FragmentListUserBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        SocketManager().apply {
            getEmitterListener(ALLUSERS, AllUser)
            mSocket = getSocket()
        }

        if (!showData) mSocket!!.emit(ALLUSERS, true)

        mBinding.swipeToRefresh.setOnRefreshListener{
            mBinding.swipeToRefresh.isRefreshing=false
            mSocket!!.emit(ALLUSERS, true)
        }

        mBinding.userList.apply {
            adapter = user_Adapter
         layoutAnimation = AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )
        }


        viewModel.dataUserLiveData.observe(viewLifecycleOwner, Observer {userlist->
            user_Adapter.data.clear()
            userlist?.forEach {users->
                if (users.username != getUser(requireContext()).username) {
                    user_Adapter.data.add(users)
                    user_Adapter.data.distinct()
                    Log.v("$TAG User Array", user_Adapter.data.toString())
                    user_Adapter.notifyDataSetChanged()
                }
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }


    private val AllUser = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            showData = true
            val mutableListType: Type = object : TypeToken<List<User>>() {}.type
            val users = Gson().fromJson<List<User>>(args[0].toString(), mutableListType)
            val userlist= removeDuplicates(users as ArrayList<User>)
            viewModel.getUsers(userlist!!)
        }
    }




    override fun onClickItem(user: User,position: Int, type: Int) {
        when (type) {
            1 -> {
                val bundle = Bundle().apply {
                    putParcelable(USER,user)
                    putInt(TYPE,1)
                }
                parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_mainFragment_to_chatFragment,bundle)
            }
        }
    }



}
