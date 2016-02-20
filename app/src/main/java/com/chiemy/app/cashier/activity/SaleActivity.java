package com.chiemy.app.cashier.activity;

import android.os.Bundle;

import com.chiemy.app.cashier.R;
import com.chiemy.zxing.activity.CaptureActivity;
import com.google.zxing.Result;

/**
 * 售货
 */
public class SaleActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sale;
    }

    @Override
    public void handleDecode(Result rawResult, Bundle bundle) {

    }
}
