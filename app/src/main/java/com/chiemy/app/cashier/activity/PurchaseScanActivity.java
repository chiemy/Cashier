package com.chiemy.app.cashier.activity;

import android.content.Intent;
import android.os.Bundle;

import com.chiemy.app.cashier.R;
import com.chiemy.zxing.activity.CaptureActivity;
import com.google.zxing.Result;

/**
 * 进货扫描界面
 */
public class PurchaseScanActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase;
    }

    @Override
    protected void onDecode(Result rawResult, Bundle bundle) {
        Intent intent = new Intent(this, PurchaseInfoActivity.class);
        intent.putExtra(PurchaseInfoActivity.BARCODE, rawResult.getText());
        startActivity(intent);
    }
}
