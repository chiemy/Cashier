package com.chiemy.app.cashier.listeners;

import com.chiemy.app.cashier.bean.MyUser;

/**
 * 获取用户信息监听
 * Created by chiemy on 16/2/19.
 */
public interface UserInfoListener {

    /**
     * 开始获取用户信息
     */
    void onStartGetUserInfo();

    /**
     * 获取用户信息
     * @param success 是否成功
     * @param type 平台 如果为null，则为自己平台
     * @param user
     */
    void onGetUserInfo(boolean success, String type, MyUser user);
}
