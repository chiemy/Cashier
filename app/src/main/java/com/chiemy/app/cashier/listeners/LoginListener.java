package com.chiemy.app.cashier.listeners;

import org.json.JSONObject;

/**
 * 登陆监听
 * Created by chiemy on 16/2/19.
 */
public interface LoginListener {
    /**
     * 正在登陆
     */
    void onLogining();

    /**
     * 登陆成功
     * @param type 第三方登陆平台
     * @param info
     */
    void onLoginSuccess(String type, JSONObject info);


    /**
     * 登陆失败
     * @param type 第三方登陆平台
     * @param i 失败码
     * @param s 失败信息
     */
    void onLoginFailure(String type, int i, String s);

    /**
     * 退出登陆
     */
    void onLogout(String type);

}
