package com.chiemy.app.cashier;

import android.app.Application;
import android.content.Context;

import com.chiemy.app.cashier.bean.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by chiemy on 16/2/19.
 */
public class CustomApplication extends Application{
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static MyUser getUser(){
        return BmobUser.getCurrentUser(context, MyUser.class);
    }
}
