package com.ix.ibrahim7.socketio.ui.fragment.dialog


import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import com.github.nkzawa.socketio.client.Socket
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ix.ibrahim7.socketio.adapter.UserAdapter
import com.ix.ibrahim7.socketio.databinding.DialogCreateGroupBinding
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.network.SocketManager
import com.ix.ibrahim7.socketio.util.Constant.GROUPNAME
import com.ix.ibrahim7.socketio.util.Constant.GROUPS
import com.ix.ibrahim7.socketio.util.Constant.ID
import com.ix.ibrahim7.socketio.util.Constant.IMAGE
import com.ix.ibrahim7.socketio.util.Constant.TAG
import com.ix.ibrahim7.socketio.util.Constant.USERGROUP
import com.ix.ibrahim7.socketio.util.Constant.convertToBase64
import com.ix.ibrahim7.socketio.util.Constant.getBitmapImage
import com.ix.ibrahim7.socketio.util.Constant.getUser
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class AddGroupDialog(val data: ArrayList<User>) : BottomSheetDialogFragment(),UserAdapter.onClick, IPickResult {

    private lateinit var mBinding: DialogCreateGroupBinding

    private val user_Adapter by lazy {
        UserAdapter(requireActivity(),data,this,2)
    }

    var image = ""
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
        mSocket = SocketManager().getSocket()
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

            tvChoose.setOnClickListener {
                openChooseImage()
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
                put(USERGROUP, array)
                put(ID, UUID.randomUUID().toString())
                put(IMAGE, image)
            }
            mSocket!!.emit(GROUPS, group)
        } catch (e: JSONException) {
            Log.v("$TAG GROUPS", "error add group " + e.message)
        }
    }

    override fun onPickResult(r: PickResult?){
        if (r!!.error == null) {
            val selectedImage = r.uri
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            image = convertToBase64(selectedImageBmp)
            mBinding.chooseImage.visibility=View.INVISIBLE
            mBinding.tvChoose.setBackgroundColor(Color.TRANSPARENT)
            mBinding.tvImageGroup.setImageBitmap(selectedImageBmp)
            getBitmapImage(requireActivity(),android.util.Base64.decode(image, android.util.Base64.DEFAULT),mBinding.tvImageGroup)
        }
    }

    private fun openChooseImage(){
        PickImageDialog.build(PickSetup().setTitle("Select Image").setSystemDialog(true))
                .setOnPickResult { onPickResult(it) }.setOnPickCancel {}.show(activity)
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