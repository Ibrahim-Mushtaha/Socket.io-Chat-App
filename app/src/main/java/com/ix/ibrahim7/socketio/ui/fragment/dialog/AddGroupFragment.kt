package com.ix.ibrahim7.socketio.ui.fragment.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.Select_User_Adapter
import com.ix.ibrahim7.socketio.model.TextMessage
import com.ix.ibrahim7.socketio.model.User
import com.ix.ibrahim7.socketio.util.Constant
import kotlinx.android.synthetic.main.dialog_create_group.*
import kotlinx.android.synthetic.main.dialog_create_group.view.*
import kotlinx.android.synthetic.main.item_full_image.*
import kotlinx.android.synthetic.main.item_full_image.view.*

class AddGroupFragment(val data: ArrayList<User>) : DialogFragment(),Select_User_Adapter.onClickListener {

    private val user_Adapter by lazy {
        Select_User_Adapter(requireActivity(),data,this)
    }

    var selectArray_checked = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_create_group, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val win=dialog!!.window
        win!!.setGravity(Gravity.CENTER)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

}