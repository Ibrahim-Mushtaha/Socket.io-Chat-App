package com.ix.ibrahim7.socketio.ui.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.GroupAdapter
import com.ix.ibrahim7.socketio.databinding.FragmentAllGroupBinding
import com.ix.ibrahim7.socketio.databinding.FragmentHomeBinding
import com.ix.ibrahim7.socketio.databinding.FragmentMainBinding
import com.ix.ibrahim7.socketio.model.Groups
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_all_group.*
import kotlinx.android.synthetic.main.fragment_all_group.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * A simple [Fragment] subclass.
 */
class AllGroupFragment : Fragment() , GroupAdapter.onClick{

    var array = ArrayList<Groups>()
    lateinit var mutableListTutorialType: Type
    private val group_adapter by lazy {
        GroupAdapter(array, this)
    }
    private lateinit var mBinding: FragmentAllGroupBinding
    private var mSocket: Socket? = null

    lateinit var root: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity().tabs.visibility=View.VISIBLE
        mBinding = FragmentAllGroupBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val app = ChatApplication()
        mSocket = app.getSocket()
        mSocket!!.on("group",AllGroups)
        mSocket!!.connect()


        list_all_group.apply {
            adapter = group_adapter
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                    requireContext(),
                    R.anim.recyclerview_layout_animation
            )
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private val AllGroups = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            mutableListTutorialType =
                    object : TypeToken<MutableList<User>>() {}.type
            val data = args[0] as JSONObject
            //group_adapter.data.clear()
            //group_adapter.data.add(Groups(data.getString("group_name"),data.getJSONObject("member").get("member")))
            //group_adapter.data.distinct()
            //group_adapter.notifyDataSetChanged()
            Log.v("ttt group", data.toString())
            Log.v("ttt group json",data.getJSONObject("member").getString("member").toString())
        }
    }


    override fun onClickItem(position: Int, type: Int) {
     when(type){
         1->{

         }
     }

    }

}
