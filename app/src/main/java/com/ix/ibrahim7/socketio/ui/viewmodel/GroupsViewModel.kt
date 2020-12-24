package com.ix.ibrahim7.socketio.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ix.ibrahim7.socketio.model.Groups
import com.ix.ibrahim7.socketio.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroupsViewModel(application: Application) : AndroidViewModel(application) {

    val dataGroupLiveData = MutableLiveData<ArrayList<Groups>>()

    fun addGroupListener(arrayList: ArrayList<Groups>) {
        GlobalScope.launch {
            dataGroupLiveData.postValue(arrayList)
        }
      }





}