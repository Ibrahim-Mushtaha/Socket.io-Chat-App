package com.ix.ibrahim7.socketio.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.GroupAdapter
import com.ix.ibrahim7.socketio.databinding.FragmentListGroupBinding
import com.ix.ibrahim7.socketio.model.Groups
import com.ix.ibrahim7.socketio.ui.viewmodel.GroupsViewModel
import com.ix.ibrahim7.socketio.network.SocketManager
import com.ix.ibrahim7.socketio.util.Constant.ALLGROUPS
import com.ix.ibrahim7.socketio.util.Constant.GROUPS
import com.ix.ibrahim7.socketio.util.Constant.TYPE
import com.ix.ibrahim7.socketio.util.Constant.getUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type

/**
 * A simple [Fragment] subclass.
 */
class ListGroupFragment : Fragment() , GroupAdapter.onClick{


    private val group_adapter by lazy {
        GroupAdapter(requireActivity(),ArrayList(), this)
    }

    private lateinit var mBinding: FragmentListGroupBinding
    private var mSocket: Socket? = null

    private var showData = false

    private val viewModel by lazy {
        ViewModelProvider(this)[GroupsViewModel::class.java]
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity().tabs.visibility=View.VISIBLE
        mBinding = FragmentListGroupBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        SocketManager().apply {
            getEmitterListener(ALLGROUPS,AllGroup)
            mSocket = getSocket()
        }

        if (!showData) mSocket!!.emit(ALLGROUPS, true)

        viewModel.dataGroupLiveData.observe(viewLifecycleOwner, Observer {groups->
            group_adapter.data.clear()
            groups.map { groupsUser ->
                groupsUser.userGroup.map { userid ->
                    if (userid == getUser(requireContext()).id) {
                        group_adapter.data.add(groupsUser)
                    }
                }
            }
            group_adapter.notifyDataSetChanged()
            if (group_adapter.data.isEmpty()) mBinding.emptyContanier.visibility = View.VISIBLE else mBinding.emptyContanier.visibility = View.GONE
        })

        mBinding.swipeToRefresh.setOnRefreshListener {
            mBinding.swipeToRefresh.isRefreshing=false
            mSocket!!.emit(ALLGROUPS, true)
        }

        mBinding.listAllGroup.apply {
            adapter = group_adapter
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                    requireContext(),
                    R.anim.recyclerview_layout_animation
            )
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private val AllGroup = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            val mutableListType: Type = object : TypeToken<List<Groups>>() {}.type
            val groups = Gson().fromJson<List<Groups>>(args[0].toString(), mutableListType)
            viewModel.addGroupListener((groups as ArrayList<Groups>?)!!)
        }
    }


    override fun onClickItem(group: Groups,position: Int, type: Int) {
     when(type){
         1->{
             val bundle = Bundle().apply {
                 putParcelable(GROUPS,group)
                 putInt(TYPE,2)
             }
             parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_mainFragment_to_chatFragment,bundle)
         }
     }

    }

}
