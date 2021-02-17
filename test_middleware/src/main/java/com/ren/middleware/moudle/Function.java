package com.ren.middleware.moudle;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.ren.middleware.Module;

public interface Function extends IProvider {
    void toFun(Activity activity);
}
