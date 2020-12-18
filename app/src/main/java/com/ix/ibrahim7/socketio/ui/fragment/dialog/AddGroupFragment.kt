package com.ix.ibrahim7.socketio.ui.fragment.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.Select_User_Adapter
import com.ix.ibrahim7.socketio.model.TextMessage
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import kotlinx.android.synthetic.main.dialog_create_group.*
import kotlinx.android.synthetic.main.dialog_create_group.view.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.item_full_image.*
import kotlinx.android.synthetic.main.item_full_image.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class AddGroupFragment(val data: ArrayList<User>) : BottomSheetDialogFragment(),Select_User_Adapter.onClickListener {

    private val user_Adapter by lazy {
        Select_User_Adapter(requireActivity(),data,this)
    }

    var selectArray_checked = ArrayList<User>()
    private var mSocket: Socket? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_create_group, container, false)
      //  dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
      //  dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      //  val win=dialog!!.window
      //  win!!.setGravity(Gravity.CENTER)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val app = ChatApplication()
        mSocket = app.getSocket()
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket!!.on(Constant.JOIN,AllUser)
        mSocket!!.connect()

        setupClickListeners(view)



    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners(view: View) {

        view.apply {

            list_select_user.apply {
                adapter = user_Adapter
            }

            btn_create.setOnClickListener {
                attemptSend(selectArray_checked)
                dismiss()
            }

        }
    }

    override fun onClickItem(position: Int, type: Int) {
        when (type) {
            1 -> {
                selectArray_checked.add(User(data[position].username))
                Log.e("eeee",data[position].username)
            }
            2->{
                selectArray_checked.remove(User(data[position].username))
                Log.e("eeee",data[position].username)
            }
        }
    }


    var onConnect = Emitter.Listener {
        Log.e("eee", "Socket Connected!")
    }
    private val onConnectError = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!")} }
    private val onDisconnect = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!")} }



    private val AllUser = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
                val data = args[0] as String
                Log.v("ttt LOGIN", data)
        }
    }


    private fun attemptSend(arrayList: ArrayList<User>) {
        try {
            val data = JSONObject().apply {
                put("member",arrayList.toString())
            }
            val group = JSONObject().apply {
                put("group_name",etxt_Group_name.text.toString().trim())
                put("member",data)
            }
            mSocket!!.emit("group", group)
        } catch (e: JSONException) {
            Log.d("me", "error send message " + e.message)
        }
    }


}