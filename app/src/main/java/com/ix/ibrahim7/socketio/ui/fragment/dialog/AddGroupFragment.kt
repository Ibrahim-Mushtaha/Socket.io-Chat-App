package com.ix.ibrahim7.socketio.ui.fragment.dialog


import android.os.Bundle
import android.util.Log
import android.view.*
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.UserAdapter
import com.ix.ibrahim7.socketio.databinding.DialogCreateGroupBinding
import com.ix.ibrahim7.socketio.databinding.FragmentHomeBinding
import com.ix.ibrahim7.socketio.model.Groups
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.GROUPNAME
import com.ix.ibrahim7.socketio.util.Constant.GROUPS
import com.ix.ibrahim7.socketio.util.Constant.ID
import com.ix.ibrahim7.socketio.util.Constant.IMAGE
import com.ix.ibrahim7.socketio.util.Constant.NAME
import com.ix.ibrahim7.socketio.util.Constant.TAG
import com.ix.ibrahim7.socketio.util.Constant.USER_GROUP
import com.ix.ibrahim7.socketio.util.Constant.getSharePref
import com.ix.ibrahim7.socketio.util.Constant.getUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_create_group.*
import kotlinx.android.synthetic.main.dialog_create_group.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class AddGroupFragment(val data: ArrayList<User>) : BottomSheetDialogFragment(),UserAdapter.onClick {

    private lateinit var mBinding: DialogCreateGroupBinding

    private val user_Adapter by lazy {
        UserAdapter(requireActivity(),data,this,2)
    }

    var selected_User = ArrayList<User>()
    private var mSocket: Socket? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogCreateGroupBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        dialog!!.setCancelable(false)
        mSocket = ChatApplication().getSocket()
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {

            listSelectUser.apply {
                adapter = user_Adapter
            }

            btnCreate.setOnClickListener {
                createNewGroup()
                dismiss()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }



    private fun createNewGroup() {
        val name = mBinding.etxtGroupName.text.toString()
        try {
            val group = JSONObject()
            val array = JSONArray()
            for (user in selected_User) {
                array.put(user.id)
            }
            array.put(getUser(requireContext()).id)
            group.apply {
                put(GROUPNAME, name)
                put(USER_GROUP, array)
                put(ID, UUID.randomUUID().toString())
                put(IMAGE, "")
            }
            mSocket!!.emit(GROUPS, group)
        } catch (e: JSONException) {
            Log.v("$TAG GROUPS", "error add group " + e.message)
        }
    }

    override fun onClickItem(user: User, position: Int, type: Int) {
        when (type) {
            1 -> {
                selected_User.add(user)
            }
            2->{
                selected_User.remove(user)
            }
        }
    }


}