package com.chiemy.app.cashier.common;

/**
 * Created by chiemy on 16/2/19.
 */
public class ErrorCode {
    /**
     * 邮箱为空
     */
    public static final int EMAIL_NUll = 101;

    /**
     * 无效的邮箱地址
     */
    public static final int EMAIL_INVALID = 102;

    /**
     * 密码为空
     */
    public static final int PASSWORD_NUll = 103;

    /**
     * 无效的密码（长度不够）
     */
    public static final int PASSWORD_INVALID = 104;

    /**
     * 密码不一致
     */
    public static final int PASSWORD_NOT_CONSISTENT = 105;

    /**
     * 用户名为null
     */
    public static final int USERNAME_NULL = 106;

}
