package com.ren.function;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ren.middleware.Module;
import com.ren.middleware.moudle.Function;

@Route(path = Module.FUNCTION)
public class FunctionOne implements Function {
    @Override
    public void toFun(Activity activity) {
        Log.d(Function.class.getName(),"   ---->  FunctionOne  ");
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(getClass().getName());
        dialog.setMessage("功能模块的实现");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertdialog1 = dialog.create();
        alertdialog1.show();
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName();
    }

    @Override
    public void init(Context context) {

    }
}
