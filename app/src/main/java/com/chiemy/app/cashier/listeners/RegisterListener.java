package com.chiemy.app.cashier.listeners;

import com.chiemy.app.cashier.bean.MyUser;

/**
 * 注册监听
 * Created by chiemy on 16/2/19.
 */
public interface RegisterListener {
    /**
     * 注册中
     */
    void onRegistering();

    /**
     * 注册成功
     */
    void onRegisterSuccess(MyUser user);

    /**
     * 注册失败
     * @param i 错误码
     * @param w 错误信息
     */
    void onRegisterFailure(int i, String msg);
}
