package com.chiemy.app.cashier.utils;

/**
 * Created by chiemy on 16/2/19.
 */
public class Util {
    /**
     * 邮箱是否有效
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * 密码是否有效
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        return password.length() > 5;
    }
}
