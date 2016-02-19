package com.chiemy.app.cashier.views;

import com.chiemy.app.cashier.listeners.LoginListener;
import com.chiemy.app.cashier.listeners.UserInfoListener;

/**
 * 登录视图
 * Created by chiemy on 16/2/19.
 */
public interface LoginView extends BaseView , LoginListener, UserInfoListener{
}
