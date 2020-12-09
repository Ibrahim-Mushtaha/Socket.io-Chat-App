package com.ix.ibrahim7.socketio.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.ix.ibrahim7.socketio.databinding.FragmentSignInBinding
import com.ix.ibrahim7.socketio.ui.activity.MainActivity
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.START
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.ix.ibrahim7.socketio.util.Constant.USERID
import com.ix.ibrahim7.socketio.util.Constant.editor
import com.ix.ibrahim7.socketio.util.Constant.setUpStatusBar
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class SignInFragment : Fragment() {


    private lateinit var mBinding: FragmentSignInBinding
    private var mSocket: Socket? = null
    lateinit var root:View

    override fun onAttach(context: Context) {
      /*  if (fire.mAuth.currentUser != null){
            startActivity(Intent(requireContext(), Main2Activity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }*/
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        setUpStatusBar(requireActivity(), 1)
        mBinding = FragmentSignInBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


      /*  val app = ChatApplication()
        mSocket = app.socket
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket!!.connect()*/

        btn_login.setOnClickListener {
            if (etxt_username_login.text.isNotEmpty()) {
                startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
                editor(requireContext()).apply {
                    putString(USER,etxt_username_login.text.toString().trim())
                    putString(USERID,UUID.randomUUID().toString())
                    putBoolean(START,true)
                    apply()
                }
              //  attemptSend()
            }else{
                Toast.makeText(requireContext(), "check your Input", Toast.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    var onConnect = Emitter.Listener {
        Log.e("eee", "Socket Connected!")
    }
    private val onConnectError = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!")} }
    private val onDisconnect = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!")} }





    private fun attemptSend() {
        val message: String = etxt_username_login.text.toString().trim()
        if (TextUtils.isEmpty(message)) {
            return
        }
        try {
            mSocket!!.emit("join", etxt_username_login.text.toString().trim())
        } catch (e: JSONException) {
            Log.d("me", "error send message " + e.message)
        }
        etxt_username_login.setText("")
       // mSocket!!.emit("msgS", message)
    }

}
