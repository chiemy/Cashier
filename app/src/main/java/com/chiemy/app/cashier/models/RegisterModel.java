package com.chiemy.app.cashier.models;

import android.content.Context;
import android.text.TextUtils;

import com.chiemy.app.cashier.R;
import com.chiemy.app.cashier.bean.MyUser;
import com.chiemy.app.cashier.common.ErrorCode;
import com.chiemy.app.cashier.listeners.RegisterListener;
import com.chiemy.app.cashier.utils.Util;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chiemy on 16/2/19.
 */
public class RegisterModel implements BaseModel {
    private RegisterListener registerListener;

    /**
     * 注册
     *
     * @param context
     * @param email 用户注册信息
     * @param userName 用户名
     * @param password 密码
     * @param confirmPassword 确认密码
     * @param listener
     */
    public void register(Context context, String email, String userName, String password,
                         String confirmPassword, final RegisterListener listener) {
        int errorCode = -1;
        int errorMsgId = -1;
        if (TextUtils.isEmpty(email)) {
            errorCode = ErrorCode.EMAIL_NUll;
            errorMsgId = R.string.error_field_required;
        }else if(TextUtils.isEmpty(userName)){
            errorCode = ErrorCode.USERNAME_NULL;
            errorMsgId = R.string.error_field_required;
        }else if(TextUtils.isEmpty(password)){
            errorCode = ErrorCode.PASSWORD_NUll;
            errorMsgId = R.string.error_field_required;
        }else if (!Util.isEmailValid(email)){
            errorCode = ErrorCode.EMAIL_INVALID;
            errorMsgId = R.string.error_invalid_email;
        }else if (!Util.isPasswordValid(password)){
            errorCode = ErrorCode.PASSWORD_INVALID;
            errorMsgId = R.string.error_invalid_password;
        }else if(!TextUtils.equals(password, confirmPassword)){
            errorCode = ErrorCode.PASSWORD_NOT_CONSISTENT;
            errorMsgId = R.string.error_passwords_not_consistent;
        }
        if (errorCode > 0){
            if (listener != null){
                listener.onRegisterFailure(errorCode, context.getString(errorMsgId));
            }
            return;
        }

        MyUser user = new MyUser();
        user.setEmail(email);
        user.setUsername(userName);
        user.setPassword(password);
        user.pwd = password;
        register(context, user, listener);
    }

    /**
     * 注册
     *
     * @param context
     * @param user     用户注册信息
     * @param listener
     */
    private void register(Context context, final MyUser user, final RegisterListener listener) {
        registerListener = listener;
        if (listener != null) {
            listener.onRegistering();
        }
        //注意：不能用save方法进行注册
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                if (registerListener != null) {
                    registerListener.onRegisterSuccess(user);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (registerListener != null) {
                    registerListener.onRegisterFailure(i, s);
                }
            }
        });
    }

    @Override
    public void destroy() {
        registerListener = null;
    }
}
