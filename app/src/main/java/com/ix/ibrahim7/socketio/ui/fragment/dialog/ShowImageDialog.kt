package com.ix.ibrahim7.socketio.ui.fragment.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.model.Message
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.decodeImage
import kotlinx.android.synthetic.main.item_full_image.*
import kotlinx.android.synthetic.main.item_full_image.view.*

class ShowImageDialog(val data: ArrayList<Message>, val position:Int) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_full_image, container, false)
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
        full_image.setImageBitmap(decodeImage(data[position].message))
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners(view: View) {

        view.btn_close.setOnClickListener {
            dismiss()
        }

    }

}