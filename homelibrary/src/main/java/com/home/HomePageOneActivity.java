package com.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ren.middleware.Module;
import com.ren.middleware.ResourceUtils;

@Route(path = Module.HOME)
public class HomePageOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = ResourceUtils.getLayoutId(getApplication(), "activity_home_page_one");
        setContentView(layoutId);
    }
}