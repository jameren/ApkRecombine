package com.home;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ren.middleware.Module;
import com.ren.middleware.moudle.HomePage;

public class HomePageOneImpl implements HomePage {
    @Override
    public void startHomePage(Context context) {
        //context.startActivity(new Intent(context, HomePageOneActivity.class));
        ARouter.getInstance().build(Module.LOGIN).navigation();
    }
}
