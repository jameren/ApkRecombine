package com.ren.packagetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ren.middleware.Module;
import com.ren.middleware.ModuleConfig;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ModuleConfig.getInstance().init(getApplication());
    }

    public void startLogin(View view) {
       ARouter.getInstance().build(Module.LOGIN).navigation();
    }

    public void startHome(View view) {
       /* try {
            ModuleConfig.getInstance().getHomePage().startHomePage(this);
        } catch (ModuleConfig.NotFondImplException e) {
            Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }*/
     ARouter.getInstance().build(Module.HOME).navigation();

    }
}