package com.chiemy.app.cashier;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.chiemy.app.cashier.bean.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by chiemy on 16/2/19.
 */
public class CustomApplication extends Application{
    public static final String DEFAULT_USER_ID = "default_user_id";
    private static Context context;
    private static MyUser defaultUser;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static MyUser getUser(){
        MyUser user = BmobUser.getCurrentUser(context, MyUser.class);
        if (user == null){
            user = getDefaultUser();
        }
        return user;
    }

    private static MyUser getDefaultUser(){
        if (defaultUser == null){
            defaultUser = new MyUser();
            defaultUser.setUsername(context.getString(R.string.sign_in));
            defaultUser.setObjectId(DEFAULT_USER_ID);
        }
        return defaultUser;
    }

    /**
     * 当前用户是否为默认用户
     * @return
     */
    public static boolean isDefaultUser(){
        return TextUtils.equals(getUser().getObjectId(), DEFAULT_USER_ID);
    }
}
