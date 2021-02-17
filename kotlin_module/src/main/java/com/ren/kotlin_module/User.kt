package com.ren.kotlin_module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable

class User(userName: String) : BaseObservable() {

    @get:Bindable
    var userName: String = ""
        set(value) {
            field = value;
            notifyPropertyChanged(BR.userName)
        }

    init {
        this.userName = userName;
    }

    @get:Bindable
    var userInfo: UserInfo? = null
        set(value) {
            field = value;
            notifyPropertyChanged(BR.userInfo)
        }

     class UserInfo(phoneNumber: String) :BaseObservable(){
        var phoneNumber: String? = null
        var nickname: String? = null

        @get:Bindable
        var gender: String? = null
        set(value){
            field = value;
            notifyPropertyChanged(BR.gender)
        }

        @get:Bindable
        var age: Int? = null
            set(value){
                field = value;
                notifyPropertyChanged(BR.age)
            }

        init {
            this.phoneNumber = phoneNumber;
        }

         override fun toString(): String {
             return "UserInfo(phoneNumber=$phoneNumber, nickname=$nickname)"
         }

     }


}