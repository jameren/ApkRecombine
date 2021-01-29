package com.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ren.middleware.ResourceUtils;

public class HomePageOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = ResourceUtils.getLayoutId(getApplication(), "activity_home_page_one");
        setContentView(layoutId);
    }
}