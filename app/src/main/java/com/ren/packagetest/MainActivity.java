package com.ren.packagetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ren.middleware.moudle.Function;
import com.ren.middleware.Module;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // ModuleConfig.getInstance().init(getApplication());
    }

    public void startLogin(View view) {
       ARouter.getInstance().build(Module.LOGIN).navigation();
    }

    public void startHome(View view) {
     ARouter.getInstance().build(Module.HOME).navigation();

    }

    public void startImpl(View view) {
        Function navigation = (Function) ARouter.getInstance().build(Module.FUNCTION).navigation();
        if (navigation != null){
            navigation.toFun(this);
        }else{
            Toast.makeText(this,"无法找到实现类",Toast.LENGTH_SHORT).show();
        }
    }

    public void startOther(View view) {
        ARouter.getInstance().build(Module.OTHER).navigation();
    }

    public void startKotlin(View view) {
        ARouter.getInstance().build(Module.KOTLIN).navigation();
    }
}