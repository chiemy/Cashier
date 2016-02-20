package com.chiemy.app.cashier.activity;

import android.os.Bundle;

import com.chiemy.app.cashier.R;
import com.chiemy.zxing.activity.CaptureActivity;
import com.google.zxing.Result;

/**
 * 进货
 */
public class PurchaseActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase;
    }

    @Override
    public void handleDecode(Result rawResult, Bundle bundle) {
        
    }
}
