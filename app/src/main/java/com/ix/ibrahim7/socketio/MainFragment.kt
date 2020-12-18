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
import com.ix.ibrahim7.socketio.adapter.UserAdapter
import com.ix.ibrahim7.socketio.adapter.ViewPagerLogin
import com.ix.ibrahim7.socketio.databinding.FragmentMainBinding
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.ui.fragment.dialog.AddGroupFragment
import com.ix.ibrahim7.socketio.ui.fragment.home.AllGroupFragment
import com.ix.ibrahim7.socketio.ui.fragment.home.HomeFragment
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import devjdelasen.com.sidebubbles.SideBubbles
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.json.JSONException


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

        val app = ChatApplication()
        mSocket = app.getSocket()
        mSocket!!.on(Constant.JOIN, AllUser)
        mSocket!!.connect()

        setUpViewPager()


        setUpSideBubbles(array)


        super.onViewCreated(view, savedInstanceState)
    }


    private val AllUser = Emitter.Listener { args ->
        requireActivity().runOnUiThread(Runnable {
            val data = args[0] as String
            array.clear()
            array.distinct()
            array.add(0, User(data))
            Log.v("ttt", data)
        })
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
        sideBubbles.addItem("group", R.drawable.ic_group, ContextCompat.getColor(requireContext(), R.color.purple_700))
        sideBubbles.addItem("addGroup", R.drawable.ic_group_add, ContextCompat.getColor(requireContext(), R.color.purple_700))
        sideBubbles.setClickItemListener(object : SideBubbles.OnClickItemListener{
            override fun onClickItem(id: String) {
                when(id){
                    "group"->{
                        /*findNavController()
                            .navigate(R.id.allGroup_Fragment)*/
                    }
                    "addGroup"->{
                        AddGroupFragment(arrayList).show(childFragmentManager,"")
                    }
                }
            }

        })
    }

}