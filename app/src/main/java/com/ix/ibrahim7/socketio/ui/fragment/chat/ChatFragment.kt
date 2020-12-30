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
import com.ix.ibrahim7.socketio.adapter.MessageAdapter
import com.ix.ibrahim7.socketio.databinding.FragmentChatBinding
import com.ix.ibrahim7.socketio.model.Groups
import com.ix.ibrahim7.socketio.model.Message
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.ui.fragment.dialog.ShowImageDialog
import com.ix.ibrahim7.socketio.network.SocketManager
import com.ix.ibrahim7.socketio.util.Constant.DES_ID
import com.ix.ibrahim7.socketio.util.Constant.GROUPS
import com.ix.ibrahim7.socketio.util.Constant.IMAGE
import com.ix.ibrahim7.socketio.util.Constant.MESSAGE
import com.ix.ibrahim7.socketio.util.Constant.SOURCE_ID
import com.ix.ibrahim7.socketio.util.Constant.TEXT
import com.ix.ibrahim7.socketio.util.Constant.TYPE
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.ix.ibrahim7.socketio.util.Constant.convertToBase64
import com.ix.ibrahim7.socketio.util.Constant.getUser
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment(), MessageAdapter.onClick, IPickResult {


    private lateinit var mBinding: FragmentChatBinding

    private var mSocket: Socket? = null
    var image = ""
    var desID=""

    private val adapter by lazy {
        MessageAdapter(requireActivity(), ArrayList(), this)
    }


    private val user_details by lazy {
        requireArguments().getParcelable<User>(USER)!!
    }

    private val group_details by lazy {
        requireArguments().getParcelable<Groups>(GROUPS)!!
    }

    private val getType by lazy {
        requireArguments().getInt(TYPE)
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

        when(getType){
            1->requireActivity().toolbar.title =user_details.username
            else -> requireActivity().toolbar.title =group_details.name
        }

        chat_list.adapter = adapter

        SocketManager().apply {
            getEmitterListener(MESSAGE, onNewMessage)
            mSocket = getSocket()
        }

        mBinding.btnSend.setOnClickListener {
            attemptSend(etxt_massege.text.toString(), TEXT)
        }

        mBinding.btnSendImage.setOnClickListener {
            openChooseImage()
        }


    }


    override fun onClickItem(position: Int, type: Int) {
        when (type) {
            1 -> {
                  ShowImageDialog(adapter.data as ArrayList<Message>,position).show(childFragmentManager,"")
            }
            2 -> {
                ShowImageDialog(adapter.data as ArrayList<Message>,position).show(childFragmentManager,"")
            }
        }
    }






    private val onNewMessage = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val data =args[0] as JSONObject
                when(getType) {
                  1->{
                      if (data.getString(DES_ID).equals(getUser(requireContext()).id) && data.getString(SOURCE_ID).equals(user_details.id)) {
                          when (data.getString(TYPE)) {
                              TEXT -> {
                                  adapter.data.add(0,
                                          Message(
                                                  data.getString(MESSAGE),
                                                  data.getString(SOURCE_ID),
                                                  Calendar.getInstance().time,
                                                  TEXT
                                          )
                                  )
                              }
                              else -> {
                                  adapter.data.add(0,
                                          Message(
                                                  data.getString(MESSAGE),
                                                  data.getString(SOURCE_ID),
                                                  Calendar.getInstance().time,
                                                  IMAGE
                                          )
                                  )
                              }
                          }
                          adapter.notifyDataSetChanged()
                          Log.e("ttt message ", data.toString())
                      }
                  }
                    2-> {
                        if (data.getString(DES_ID).contains(getUser(requireContext()).id)) {
                            when (data.getString(TYPE)) {
                                TEXT -> {
                                    adapter.data.add(0,
                                            Message(
                                                    data.getString(MESSAGE),
                                                    data.getString(SOURCE_ID),
                                                    Calendar.getInstance().time,
                                                    TEXT
                                            )
                                    )
                                }
                                else -> {
                                    adapter.data.add(0,
                                            Message(
                                                    data.getString(MESSAGE),
                                                    data.getString(SOURCE_ID),
                                                    Calendar.getInstance().time,
                                                    IMAGE
                                            )
                                    )
                                }
                            }
                            adapter.notifyDataSetChanged()
                            Log.e("ttt message ", data.toString())
                        }
                    }
                    }
            } catch (e: Exception) {
               Log.e("eee ex",e.message.toString())
            }

        }
    }


    private fun openChooseImage(){
        PickImageDialog.build(PickSetup().setTitle("Select Image").setSystemDialog(true))
                .setOnPickResult { onPickResult(it) }.setOnPickCancel {}.show(activity)
    }


    private fun attemptSend(message: String,type: String) {
        val message2 = JSONObject().apply {
            put(MESSAGE,message)
            put(SOURCE_ID, getUser(requireContext()).id)
            put(DES_ID,getDesID(getType))
            put(TYPE,type)
        }

        when(getType) {
                1->{
                    adapter.data.add(0,Message(message, getUser(requireContext()).id, Calendar.getInstance().time, type))
                    adapter.notifyDataSetChanged ()
                    }
        }
        mSocket !!. emit (MESSAGE, message2)
        etxt_massege.setText("")
    }

    override fun onPickResult(r: PickResult?){
        if (r!!.error == null) {
            val selectedImage = r.uri
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            image = convertToBase64(selectedImageBmp)
            attemptSend(image,IMAGE)
        }
    }


    private fun getDesID(type:Int):String{
        when(type){
            1->desID =user_details.id
            2->desID =group_details.userGroup.toString()
        }
        return desID
    }






}


