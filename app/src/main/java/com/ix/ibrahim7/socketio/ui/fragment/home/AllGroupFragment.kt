package com.ix.ibrahim7.socketio.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.adapter.GroupAdapter
import com.ix.ibrahim7.socketio.model.Groups
import kotlinx.android.synthetic.main.fragment_all_group.view.*

/**
 * A simple [Fragment] subclass.
 */
class AllGroupFragment : Fragment() , GroupAdapter.onClick{

    var array = ArrayList<Groups>()



    private val adapter by lazy {
        GroupAdapter(array, this)
    }

    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            R.layout.fragment_all_group, container, false
        )
        root = binding.root
        root.apply {




        }
        return root
    }




    override fun onClickItem(position: Int, type: Int) {
     when(type){
         1->{

         }
     }

    }

}
