package com.ren.kotlin_module.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ren.kotlin_module.User

class LoginViewModel:ViewModel(){

    var user = MutableLiveData<User>();
    var userName = MutableLiveData<String>();
    var password = MutableLiveData<String>();

    fun login(){
        var userNameStr = userName.value;
        var passwordStr = password.value;
        Log.d("LoginViewModel",userNameStr+"  ----  "+passwordStr);
        val userInfo = User.UserInfo("13692190325");
        userInfo.gender = "男";
        userInfo.age = 27;
        val user = User(userNameStr!!);
        user.userInfo = userInfo;
        this.user.value = user;

    }
}