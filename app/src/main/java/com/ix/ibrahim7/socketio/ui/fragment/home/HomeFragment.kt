package com.ix.ibrahim7.socketio.ui.fragment.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.UserAdapter
import com.ix.ibrahim7.socketio.databinding.FragmentHomeBinding
import com.ix.ibrahim7.socketio.model.Groups
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.ALLUSERS
import com.ix.ibrahim7.socketio.util.Constant.JOIN
import com.ix.ibrahim7.socketio.util.Constant.TAG
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.ix.ibrahim7.socketio.util.Constant.getUser
import devjdelasen.com.sidebubbles.SideBubbles
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.lang.reflect.Type


class HomeFragment : Fragment(), UserAdapter.onClick {

    private lateinit var mBinding: FragmentHomeBinding

    private val user_Adapter by lazy {
        UserAdapter(requireActivity(),ArrayList(), this,1)
    }

    private var mSocket: Socket? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().tabs.visibility=View.VISIBLE
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ChatApplication().apply {
            getEmitterListener(ALLUSERS, AllUser)
            mSocket = getSocket()
        }


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


        super.onViewCreated(view, savedInstanceState)
    }


    private val AllUser = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            val mutableListType: Type = object : TypeToken<List<User>>() {}.type
            val users = Gson().fromJson<List<User>>(args[0].toString(), mutableListType)
            users.forEach {users->
                if (users.username != getUser(requireContext()).username) {
                    user_Adapter.data.clear()
                    user_Adapter.data.add(users) // id
                    user_Adapter.data.distinct()
                    Log.v("$TAG User Array", user_Adapter.data.toString())
                    user_Adapter.notifyDataSetChanged()
                }
            }

        }
    }




    override fun onClickItem(user: User,position: Int, type: Int) {
        when (type) {
            1 -> {
                val bundle = Bundle().apply {
                    putParcelable(USER,user)
                }
                parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_mainFragment_to_chatFragment,bundle)
            }
        }
    }



}
