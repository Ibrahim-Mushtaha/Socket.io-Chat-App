package com.ix.ibrahim7.socketio.ui.fragment.auth

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
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.ui.activity.MainActivity
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.ID
import com.ix.ibrahim7.socketio.util.Constant.JOIN
import com.ix.ibrahim7.socketio.util.Constant.NAME
import com.ix.ibrahim7.socketio.util.Constant.START
import com.ix.ibrahim7.socketio.util.Constant.TAG
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.ix.ibrahim7.socketio.util.Constant.USERID
import com.ix.ibrahim7.socketio.util.Constant.editor
import com.ix.ibrahim7.socketio.util.Constant.setUpStatusBar
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class SignInFragment : Fragment() {


    private lateinit var mBinding: FragmentSignInBinding
    private var mSocket: Socket? = null
    lateinit var root: View

    private val id = UUID.randomUUID().toString()

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


        ChatApplication().apply {
            getEmitterListener(Socket.EVENT_CONNECT_ERROR, onConnectError)
            getEmitterListener(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            getEmitterListener(Socket.EVENT_CONNECT, onConnect)
            getEmitterListener(Socket.EVENT_DISCONNECT, onDisconnect)
            getSocket()!!.connect()
        }

        btn_login.setOnClickListener {
            if (etxt_username_login.text.isNotEmpty()) {
                startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
                attemptSend()
            } else {
                Toast.makeText(requireContext(), "check your Input", Toast.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    var onConnect = Emitter.Listener {}
    private val onConnectError = Emitter.Listener {}
    private val onDisconnect = Emitter.Listener {}


    private fun attemptSend() {
        val message: String = etxt_username_login.text.toString().trim()
        if (TextUtils.isEmpty(message)) {
            return
        }
        try {
            val user = JSONObject().apply {
                put(NAME, etxt_username_login.text.toString().trim())
                put(ID, id)
            }
            editor(requireContext()).apply {
                putString(USER, user.toString())
                putBoolean(START, true)
                apply()
            }
            mSocket!!.emit(JOIN, user)
        } catch (e: JSONException) {
            Log.d("$TAG JOIN", "error join" + e.message)
        }
    }

}
