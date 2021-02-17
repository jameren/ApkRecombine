package com.ren.middleware;

import android.content.Context;
import android.util.Log;

import com.ren.middleware.moudle.HomePage;
import com.ren.middleware.moudle.LoginPage;

public class ModuleConfig implements Module{
    private HomePage homePage;
    private LoginPage loginPage;
    private Context context;

    private ModuleConfig() {
    }

    private static ModuleConfig moduleConfig;

    public static synchronized ModuleConfig getInstance(){
            if (moduleConfig == null){
                moduleConfig = new ModuleConfig();
            }
            return moduleConfig;
    }
    public synchronized void init(Context context){
        this.context = context;
        scanClass(context);
    }

    private void scanClass(Context context) {
        //扫描Home页面实现
        homePage = ClassesReader.scanFirstSon(context, HomePage.class);
        //扫描Login页面实现
        loginPage = ClassesReader.scanFirstSon(context,LoginPage.class);
        if (homePage!=null) {
            Log.d("类扫描", homePage.toString());
        }
        if (loginPage!=null) {
            Log.d("类扫描", loginPage.toString());
        }
    }


    public class NotFondImplException extends Exception{
        public NotFondImplException() {
            super("没有找到相关的实现类!");
        }
    }
    public HomePage getHomePage() throws NotFondImplException{
       // scanClass(context);
        if (homePage == null){
            throw new NotFondImplException();
        }
        return homePage;
    }
    public LoginPage getLoginPage() throws NotFondImplException{
        //scanClass(context);
        if (loginPage == null){
            throw new NotFondImplException();
        }
        return loginPage;
    }
}
