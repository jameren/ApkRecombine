package com.home;

import androidx.appcompat.app.AppCompatActivity;

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
        ARouter.getInstance().build(Module.HOME).navigation();
        finish();
    }
}