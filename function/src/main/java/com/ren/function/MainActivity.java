package com.ren.function;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ren.middleware.Module;
import com.ren.middleware.moudle.Function;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Function navigation = (Function) ARouter.getInstance().build(Module.FUNCTION).navigation();
        if (navigation != null){
            navigation.toFun(this);
        }else{
            Toast.makeText(this,"无法找到实现类",Toast.LENGTH_SHORT).show();
        }
    }
}