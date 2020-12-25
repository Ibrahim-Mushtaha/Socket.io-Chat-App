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
import com.github.nkzawa.socketio.client.Socket
import com.ix.ibrahim7.socketio.databinding.FragmentSignInBinding
import com.ix.ibrahim7.socketio.ui.activity.MainActivity
import com.ix.ibrahim7.socketio.util.SocketConnection
import com.ix.ibrahim7.socketio.util.Constant.ID
import com.ix.ibrahim7.socketio.util.Constant.IMAGE
import com.ix.ibrahim7.socketio.util.Constant.ISONLINE
import com.ix.ibrahim7.socketio.util.Constant.JOIN
import com.ix.ibrahim7.socketio.util.Constant.NAME
import com.ix.ibrahim7.socketio.util.Constant.START
import com.ix.ibrahim7.socketio.util.Constant.TAG
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.ix.ibrahim7.socketio.util.Constant.editor
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class SignInFragment : Fragment() {


    private lateinit var mBinding: FragmentSignInBinding
    private var mSocket: Socket? = null

    private val id = UUID.randomUUID().toString()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        mBinding = FragmentSignInBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        SocketConnection().apply {
            mSocket = getSocket()
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


    private fun attemptSend() {
        val message: String = etxt_username_login.text.toString().trim()
        if (TextUtils.isEmpty(message)) {
            return
        }
        try {
            val user = JSONObject().apply {
                put(NAME, etxt_username_login.text.toString().trim())
                put(ID, id)
                put(IMAGE, "")
                put(ISONLINE, true)
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
