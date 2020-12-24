package com.ix.ibrahim7.socketio.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ix.ibrahim7.socketio.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val dataUserLiveData = MutableLiveData<ArrayList<User>>()

    fun getUsers(arrayList: ArrayList<User>) {
        GlobalScope.launch {
            dataUserLiveData.postValue(arrayList)
        }
      }





}