package com.ren.loginlibrary;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ren.middleware.Module;
import com.ren.middleware.moudle.LoginPage;

public class LoginPageImpl implements LoginPage {
    @Override
    public void startLoginPage(Context context) {
       // context.startActivity(new Intent(context, LoginActivity.class));
        //ARouter.getInstance().build(Module.LOGIN).navigation();
    }
}
