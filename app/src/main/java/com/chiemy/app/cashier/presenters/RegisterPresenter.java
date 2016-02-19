package com.chiemy.app.cashier.presenters;

import android.content.Context;

import com.chiemy.app.cashier.bean.MyUser;
import com.chiemy.app.cashier.models.RegisterModel;
import com.chiemy.app.cashier.views.RegisterView;

/**
 * 注册Presenter
 * Created by chiemy on 16/2/19.
 */
public class RegisterPresenter implements BasePresenter{
    private RegisterView view;
    private RegisterModel model;

    public RegisterPresenter(RegisterView view){
        this.view = view;
        model = new RegisterModel();
    }

    public void register(Context context, String email, String userName, String password,
                         String confirmPassword) {
        model.register(context, email, userName, password, confirmPassword, view);
    }


    /**
     * 注册
     * @param context
     * @param user 用户
     */
    private void register(Context context, MyUser user){
        //model.register(context, user, view);
    }

    @Override
    public void destroy() {
        model.destroy();
        view = null;
    }
}
