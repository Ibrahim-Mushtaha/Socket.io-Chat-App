package com.ix.ibrahim7.socketio.ui.fragment.chat


import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.ix.ibrahim7.socketio.adapter.Message_Adapter
import com.ix.ibrahim7.socketio.databinding.FragmentChatBinding
import com.ix.ibrahim7.socketio.model.TextMessage
import com.ix.ibrahim7.socketio.ui.fragment.dialog.ShowImageFragment
import com.ix.ibrahim7.socketio.util.ChatApplication
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.IMAGE
import com.ix.ibrahim7.socketio.util.Constant.MESSAGE
import com.ix.ibrahim7.socketio.util.Constant.TEXT
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment(), Message_Adapter.onClick, IPickResult {


    private lateinit var mBinding: FragmentChatBinding

    private var mSocket: Socket? = null

    var image = ""

    private val adapter by lazy {
        Message_Adapter(requireActivity(), ArrayList(), this)
    }

    private val arg by lazy {
        requireArguments().getString("Des_id")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity().tabs.visibility=View.GONE
        setHasOptionsMenu(true)
        mBinding = FragmentChatBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar.title =arg
        chat_list.adapter = adapter

        val app = ChatApplication()
        mSocket = app.getSocket()
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket!!.on(MESSAGE, onNewMessage)
        mSocket!!.connect()



        btn_send.setOnClickListener {
            attemptSend(etxt_massege.text.toString(), TEXT)
        }

        btn_send_image.setOnClickListener {
            openChooseImage()
        }


    }


    override fun onClickItem(position: Int, type: Int) {
        when (type) {
            1 -> {
                  ShowImageFragment(adapter.data as ArrayList<TextMessage>,position).show(childFragmentManager,"")
            }
            2 -> {
                ShowImageFragment(adapter.data as ArrayList<TextMessage>,position).show(childFragmentManager,"")
            }
        }
    }



    var onConnect = Emitter.Listener {
        Log.e("eee", "Socket Connected!")
    }

    private val onConnectError = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!") } }
    private val onDisconnect = Emitter.Listener { requireActivity().runOnUiThread { Log.e("eee", "Socket Connected!") } }





    private val onNewMessage = Emitter.Listener { args ->
        requireActivity().runOnUiThread(Runnable {
            val data =args[0] as JSONObject
            try {

              if (data.getString("des_id").equals(Constant.getSharePref(requireContext()).getString(USER,"")) && data.getString("source_id").equals(arg)){
                  if (data.getString("type") == Constant.TEXT) {
                      adapter.data.add(
                          TextMessage(
                              data.getString("message"),
                              data.getString("source_id"),
                              Calendar.getInstance().time,
                              TEXT
                          )
                      )
                  }else{
                      adapter.data.add(
                          TextMessage(
                              data.getString("message"),
                              data.getString("source_id"),
                              Calendar.getInstance().time,
                              IMAGE
                          )
                      )
                  }
                    adapter.notifyDataSetChanged()
                Log.e("ttt message ", data.toString())
                }else {
                    Log.e("ttt", data.getString("des_id"))
                }
            } catch (e: Exception) {
               Log.e("eee ex",e.message.toString())
            }

        })
    }


    private fun openChooseImage(){
        PickImageDialog.build(PickSetup().setTitle("Select Image").setSystemDialog(true))
                .setOnPickResult { onPickResult(it) }.setOnPickCancel {}.show(activity)
    }


    private fun attemptSend(message: String,type: String) {
        val message2 = JSONObject().apply {
            put("message",message)
            put("source_id",Constant.getSharePref(requireContext()).getString(USER,""))
            put("des_id",arg)
            put("type",type)
        }
        adapter.data.add(TextMessage(message.toString(),Constant.getSharePref(requireContext()).getString(USER,"").toString(), Calendar.getInstance().time, type))
        adapter.notifyDataSetChanged()
        mSocket!!.emit("message", message2)
        etxt_massege.setText("")
    }

    override fun onPickResult(r: PickResult?){
        if (r!!.error == null) {
            val selectedImage = r.uri
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            image = ImageUpload(selectedImageBmp)

            attemptSend(image,IMAGE)
        }
    }

    private fun ImageUpload(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val image: String =
                android.util.Base64.encodeToString(
                        byteArrayOutputStream.toByteArray(),
                        android.util.Base64.DEFAULT
                )
        return image
    }



}


