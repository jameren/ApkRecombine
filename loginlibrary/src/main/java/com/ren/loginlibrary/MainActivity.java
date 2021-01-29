package com.ren.loginlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ren.middleware.Module;
import com.ren.middleware.ModuleConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this,LoginActivity.class));
        ModuleConfig.getInstance().init(this);
        try {
            ModuleConfig.getInstance().getLoginPage().startLoginPage(this);
        } catch (ModuleConfig.NotFondImplException e) {
            Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}