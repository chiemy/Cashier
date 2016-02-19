package com.chiemy.app.cashier.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chiemy.app.cashier.R;
import com.chiemy.app.cashier.bean.MyUser;
import com.chiemy.app.cashier.common.ErrorCode;
import com.chiemy.app.cashier.common.MyConstants;
import com.chiemy.app.cashier.presenters.RegisterPresenter;
import com.chiemy.app.cashier.views.RegisterView;

public class RegisterActivity extends BaseActivity implements RegisterView, View.OnClickListener {
    private EditText emailEt;
    private EditText userNameEt;
    private EditText passwordEt;
    private EditText passwordConfirmEt;

    private View registerFormView;
    private View progressView;

    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerPresenter = new RegisterPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailEt = (EditText) findViewById(R.id.et_register_email);
        userNameEt = (EditText) findViewById(R.id.et_username);
        passwordEt = (EditText) findViewById(R.id.et_register_pwd);
        passwordConfirmEt = (EditText) findViewById(R.id.et_confirm_pwd);
        passwordConfirmEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(this);

        registerFormView = findViewById(R.id.register_form);
        progressView = findViewById(R.id.register_progress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                attemptRegister();
                break;
        }
    }

    private void attemptRegister() {
        String email = emailEt.getText().toString();
        String userName = userNameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String confirmPassword = passwordConfirmEt.getText().toString();

        registerPresenter.register(this, email, userName, password, confirmPassword);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            registerFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onRegistering() {
        emailEt.setError(null);
        userNameEt.setError(null);
        passwordEt.setError(null);
        passwordConfirmEt.setError(null);
        showProgress(true);
    }

    @Override
    public void onRegisterSuccess(MyUser user) {
        showProgress(false);
        Intent intent = getIntent();
        intent.putExtra(MyConstants.EXTRA_REGISTER_USER, user);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRegisterFailure(int i, String msg) {
        showProgress(false);
        switch (i) {
            case ErrorCode.EMAIL_INVALID:
            case ErrorCode.EMAIL_NUll:
                emailEt.setError(msg);
                emailEt.requestFocus();
                break;
            case ErrorCode.USERNAME_NULL:
                userNameEt.setError(msg);
                userNameEt.requestFocus();
                break;
            case ErrorCode.PASSWORD_INVALID:
            case ErrorCode.PASSWORD_NUll:
                passwordEt.setError(msg);
                passwordEt.requestFocus();
                break;
            case ErrorCode.PASSWORD_NOT_CONSISTENT:
                passwordConfirmEt.requestFocus();
                passwordConfirmEt.setError(msg);
                break;
            default:
                Snackbar.make(registerFormView, msg + "(" + i + ")", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerPresenter.destroy();
    }
}
