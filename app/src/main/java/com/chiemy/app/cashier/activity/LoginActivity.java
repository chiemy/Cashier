package com.chiemy.app.cashier.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chiemy.app.cashier.R;
import com.chiemy.app.cashier.bean.MyUser;
import com.chiemy.app.cashier.common.ErrorCode;
import com.chiemy.app.cashier.common.MyConstants;
import com.chiemy.app.cashier.presenters.LoginPresenter;
import com.chiemy.app.cashier.views.LoginView;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import cn.bmob.v3.BmobUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginView, OnClickListener{
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //或者
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginPresenter = new LoginPresenter(this);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        findViewById(R.id.tv_login_with_qq).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login_with_qq:
                loginPresenter.loginWithQQ(this);
                break;
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
        }
    }

    private void attemptLogin() {
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        loginPresenter.login(this, email, password);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onLogining() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
        showProgress(true);
    }

    @Override
    public void onLoginSuccess(String type, JSONObject object) {
        showProgress(false);
        if (TextUtils.equals(type, BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ)){
            loginPresenter.getQQUserInfo(this);
        }else{
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onLoginFailure(String type, int i, String s) {
        showProgress(false);
        switch (i){
            case ErrorCode.EMAIL_NUll:
            case ErrorCode.EMAIL_INVALID:
                mEmailView.setError(s);
                mEmailView.requestFocus();
                break;
            case ErrorCode.PASSWORD_NUll:
            case ErrorCode.PASSWORD_INVALID:
                mPasswordView.setError(s);
                mPasswordView.requestFocus();
                break;
            default:
                Snackbar.make(mLoginFormView, s + "(" + i + ")", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLogout(String type) {
    }


    @Override
    public void onStartGetUserInfo() {
        showGetUserInfoProgress(true);
    }

    @Override
    public void onGetUserInfo(boolean success, String type, MyUser user) {
        showGetUserInfoProgress(false);
        if (TextUtils.equals(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, type)){
            if (success){
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private ProgressDialog progressDialog;
    private void showGetUserInfoProgress(boolean show) {
        if (show){
            progressDialog = showProgressDialog(getString(R.string.loading_user_info));
        }else if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showProgress(false);
        if (resultCode == RESULT_OK){
            if (requestCode == Constants.REQUEST_LOGIN){
                Tencent.onActivityResultData(requestCode, resultCode, data, null);
            } else if (requestCode == REQUEST_REGISTER){
                MyUser user = (MyUser) data.getSerializableExtra(MyConstants.EXTRA_REGISTER_USER);
                loginPresenter.login(this, user.getEmail(), user.pwd);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private static final int REQUEST_REGISTER = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_register){
            startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), REQUEST_REGISTER);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (progressDialog != null && progressDialog.isShowing()){
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.destroy();
    }
}

