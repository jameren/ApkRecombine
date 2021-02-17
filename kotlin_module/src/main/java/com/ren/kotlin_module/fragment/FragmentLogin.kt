package com.ren.kotlin_module.fragment

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ren.kotlin_module.R
import com.ren.kotlin_module.User
import com.ren.kotlin_module.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

class FragmentLogin : Fragment() {
    private var loginViewModel:LoginViewModel? = null;
    private lateinit var  bind:FragmentLoginBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java);
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         bind = FragmentLoginBinding.inflate(inflater,container,false);
        bind.loginVm = loginViewModel;
        return bind.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent();
        initData();
    }

    private fun initEvent() {
        bt_Login.setOnClickListener {

            loginViewModel!!.login();
        }
    }

    private fun initData() {
        loginViewModel!!.user.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, loginViewModel!!.user.value!!.userInfo.toString(), Toast.LENGTH_SHORT).show()
            bind.userInfo = loginViewModel!!.user.value!!.userInfo;
        })
    }

}