package com.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.ren.middleware.ModuleConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ModuleConfig.getInstance().init(getApplication());
        try {
            ModuleConfig.getInstance().getHomePage().startHomePage(this);
        } catch (ModuleConfig.NotFondImplException e) {
            Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }finally {
            finish();
        }

    }
}