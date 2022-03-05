package com.wac.labcollect.ui.activity.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.wac.labcollect.ui.BaseViewModel

class MainViewModel: BaseViewModel() {
    private var _currentUser  = MutableLiveData<FirebaseUser>(null)
    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    fun updateCurrentUser(user: FirebaseUser) {
        _currentUser.postValue(user)
    }

}