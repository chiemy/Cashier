package com.chiemy.app.cashier.presenters;

import android.app.Activity;
import android.content.Context;

import com.chiemy.app.cashier.models.LoginModel;
import com.chiemy.app.cashier.views.LoginView;

/**
 * 登录
 * Created by chiemy on 16/2/19.
 */
public class LoginPresenter implements BasePresenter {
    private LoginView loginView;
    private LoginModel loginModel;

    public LoginPresenter(LoginView loginView){
        this.loginView = loginView;
        loginModel = new LoginModel();
    }

    /**
     * 登录
     * @param context
     * @param email 用户
     * @param password 密码
     */
    public void login(Context context, String email, String password){
        loginModel.login(context, email, password, loginView);
    }


    /**
     * QQ登录
     * @param activity
     */
    public void loginWithQQ(Activity activity){
        loginModel.loginWithQQ(activity, loginView);
    }

    /**
     * 退出登录
     *
     * @param context
     */
    public void logOut(String type, Context context) {
        loginModel.logOut(type, context, loginView);
    }

    public void getQQUserInfo(Context context) {
        loginModel.getQQUserInfo(context, loginView);
    }

    @Override
    public void destroy() {
        loginModel.destroy();
        loginView = null;
    }
}
