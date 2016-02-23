package com.chiemy.app.cashier.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chiemy on 16/2/19.
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    protected ProgressDialog showProgressDialog(String msg){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.show();
        return progressDialog;
    }

    protected void dismissProgress(){
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
