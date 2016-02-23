package com.chiemy.app.cashier.models;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.chiemy.app.cashier.R;
import com.chiemy.app.cashier.bean.MyUser;
import com.chiemy.app.cashier.common.ErrorCode;
import com.chiemy.app.cashier.common.MyConstants;
import com.chiemy.app.cashier.listeners.LoginListener;
import com.chiemy.app.cashier.listeners.UserInfoListener;
import com.chiemy.app.cashier.utils.Util;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 登录
 * Created by chiemy on 16/2/19.
 */
public class LoginModel implements BaseModel {
    private LoginListener loginListener;

    private Tencent tencent;

    public LoginModel(){
    }

    /**
     * 登陆
     * @param context
     * @param email 邮箱
     * @param password 密码
     */
    public void login(Context context, String email, String password, final LoginListener listener) {
        int errorCode = -1;
        int errorMsgId = -1;
        if (TextUtils.isEmpty(email)) {
            errorCode = ErrorCode.EMAIL_NUll;
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
        }
        if (errorCode > 0){
            if (listener != null){
                listener.onLoginFailure(null, errorCode, context.getString(errorMsgId));
            }
            return;
        }
        MyUser user = new MyUser();
        user.setUsername(email);
        user.setEmail(email);
        user.setPassword(password);
        login(context, user, listener);
    }


    /**
     * 登陆
     *
     * @param context
     * @param user 用户
     */
    public void login(Context context, MyUser user, final LoginListener listener) {
        loginListener = listener;
        if (listener != null) {
            listener.onLogining();
        }
        user.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                if (loginListener != null) {
                    loginListener.onLoginSuccess(null, null);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (loginListener != null) {
                    loginListener.onLoginFailure(null, i, s);
                }
            }
        });
    }

    public void loginWithQQ(final Activity activity, final LoginListener listener) {
        loginListener = listener;
        if (listener != null) {
            listener.onLogining();
        }
        final Context context = activity.getApplicationContext();
        if (tencent == null){
            tencent = Tencent.createInstance(MyConstants.QQ_APP_ID, context);
        }
        tencent = Tencent.createInstance(MyConstants.QQ_APP_ID, context);
        tencent.logout(context);
        tencent.login(activity, "all", new IUiListener() {

            @Override
            public void onComplete(Object o) {
                try {
                    String openid = ((JSONObject) o).getString("openid");
                    String access_token = ((JSONObject) o).getString("access_token");
                    String expires_in = ((JSONObject) o).getString("expires_in");
                    tencent.setOpenId(openid);
                    tencent.setAccessToken(access_token, expires_in);
                    LoginQQ(context, access_token, expires_in, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (loginListener != null) {
                    loginListener.onLoginFailure(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, uiError.errorCode, uiError.errorMessage);
                }
            }

            @Override
            public void onCancel() {
            }
        });

    }

    private void LoginQQ(Context context, String accessToken, String expiresIn, String userId) {
        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, accessToken, expiresIn, userId);
        BmobUser.loginWithAuthData(context, authInfo, new OtherLoginListener() {
            @Override
            public void onSuccess(JSONObject userAuth) {
                if (loginListener != null) {
                    loginListener.onLoginSuccess(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, userAuth);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (loginListener != null) {
                    loginListener.onLoginFailure(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, code, msg);
                }
            }
        });
    }

    /**
     * 退出登录
     *
     * @param context
     */
    public void logOut(String type, Context context, LoginListener listener) {
        if (TextUtils.isEmpty(type)){
            BmobUser.logOut(context);   //清除缓存用户对象
        }else if (TextUtils.equals(type, BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ)){
            Tencent.createInstance(MyConstants.QQ_APP_ID, context).logout(context);
        }
        if (listener != null){
            listener.onLogout(type);
        }
    }

    private UserInfoListener userInfoListener;

    /**
     * 获取用户信息
     * @param context
     * @param listener
     */
    public void getQQUserInfo(final Context context, final UserInfoListener listener) {
        userInfoListener = listener;
        if (userInfoListener != null){
            userInfoListener.onStartGetUserInfo();
        }
        if (tencent == null){
            tencent = Tencent.createInstance(MyConstants.QQ_APP_ID, context.getApplicationContext());
        }
        UserInfo userInfo = new UserInfo(context.getApplicationContext(), tencent.getQQToken());
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                boolean success = false;
                MyUser myUser = null;
                if (o != null){
                    JSONObject jsonObject = (JSONObject) o;
                    try {
                        int ret = jsonObject.getInt("ret");
                        success = (ret == 0);
                        if (success){
                            MyUser bmobUser = BmobUser.getCurrentUser(context, MyUser.class);
                            myUser = new MyUser();
                            if (bmobUser != null && TextUtils.isEmpty(bmobUser.avatar)){
                                myUser.avatar = jsonObject.getString("figureurl_qq_2");

                            }
                            String nickName = jsonObject.getString("nickname");
                            myUser.setUsername(nickName);
                            updateUser(context, myUser, listener);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (userInfoListener != null){
                    userInfoListener.onGetUserInfo(false, BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, null);
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void updateUser(Context context, MyUser newUser, final UserInfoListener listener){
        final MyUser bmobUser = BmobUser.getCurrentUser(context, MyUser.class);
        newUser.update(context, bmobUser.getObjectId(), new UpdateListener(){
            @Override
            public void onSuccess() {
                if (listener != null){
                    listener.onGetUserInfo(true, BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, bmobUser);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (listener != null){
                    listener.onGetUserInfo(false, BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, bmobUser);
                }
            }
        });
    }

    @Override
    public void destroy() {
        loginListener = null;
        userInfoListener = null;
    }
}
