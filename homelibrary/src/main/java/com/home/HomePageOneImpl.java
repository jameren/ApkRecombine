package com.home;

import android.content.Context;
import android.content.Intent;

import com.ren.middleware.moudle.HomePage;

public class HomePageOneImpl implements HomePage {
    @Override
    public void startHomePage(Context context) {
        context.startActivity(new Intent(context, HomePageOneActivity.class));
    }
}
