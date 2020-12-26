package com.ix.ibrahim7.socketio.ui.fragment.home

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.nkzawa.socketio.client.Socket
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.databinding.FragmentProfileBinding
import com.ix.ibrahim7.socketio.util.Constant.ID
import com.ix.ibrahim7.socketio.util.Constant.IMAGE
import com.ix.ibrahim7.socketio.util.Constant.ISONLINE
import com.ix.ibrahim7.socketio.util.Constant.NAME
import com.ix.ibrahim7.socketio.util.Constant.UPDATEPROFILE
import com.ix.ibrahim7.socketio.util.Constant.USER
import com.ix.ibrahim7.socketio.util.Constant.convertToBase64
import com.ix.ibrahim7.socketio.util.Constant.decodeImage
import com.ix.ibrahim7.socketio.util.Constant.editor
import com.ix.ibrahim7.socketio.util.Constant.getUser
import com.ix.ibrahim7.socketio.network.SocketManager
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment(), IPickResult {

    private lateinit var mBinding: FragmentProfileBinding

    private var mSocket: Socket? = null
    private val json = JSONObject()
    private var click = false
    private var profileImage = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().tabs.visibility = View.GONE
        mBinding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SocketManager().apply { mSocket = getSocket() }

        if (getUser(requireContext()).image != "") {
            mBinding.tvProfileImage.setImageBitmap(decodeImage(getUser(requireContext()).image))
        }

        profileImage = getUser(requireContext()).image
        mBinding.etxtUsername.setText(getUser(requireContext()).username)
        mBinding.etxtUsername.isEnabled = click


        json.put(ID, getUser(requireContext()).id)
        json.put(NAME, mBinding.etxtUsername.text.toString())
        json.put(ISONLINE, true)


        mBinding.btnChooseImage.setOnClickListener {
            openChooseImage()
        }

        mBinding.fabBtn.setOnClickListener {
            if (click) {
                click = false
                mBinding.fabBtn.setImageResource(R.drawable.ic_edit)
                mBinding.etxtUsername.isEnabled = click
            } else {
                click = true
                mBinding.etxtUsername.isEnabled = click
                json.put(IMAGE, profileImage)
                mSocket!!.emit(UPDATEPROFILE, json)
                editor(requireContext())!!.apply {
                    putString(USER, json.toString())
                    apply()
                }
                mBinding.fabBtn.setImageResource(R.drawable.ic_save)
            }
        }

    }


    private fun openChooseImage() {
        PickImageDialog.build(PickSetup().setTitle("Select Image").setSystemDialog(true))
            .setOnPickResult { onPickResult(it) }.setOnPickCancel {}.show(activity)
    }

    override fun onPickResult(r: PickResult?) {
        if (r!!.error == null) {
            val selectedImage = r.uri
            val selectedImageBmp =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            profileImage = convertToBase64(selectedImageBmp)
            mBinding.tvProfileImage.setImageBitmap(selectedImageBmp)
            json.put(IMAGE, profileImage)
            mSocket!!.emit(UPDATEPROFILE, json)
            editor(requireContext())!!.apply {
                putString(USER, json.toString())
                apply()
            }
        }
    }

}