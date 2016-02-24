package com.chiemy.app.cashier.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chiemy.app.cashier.CustomApplication;
import com.chiemy.app.cashier.R;
import com.chiemy.app.cashier.bean.Goods;
import com.chiemy.app.cashier.bean.MyUser;
import com.chiemy.app.cashier.bean.PurchaseRecord;
import com.chiemy.app.cashier.db.DBCallback;
import com.chiemy.app.cashier.db.DBUpdateCallback;
import com.chiemy.app.cashier.db.GoodsDAO;
import com.chiemy.app.cashier.db.PurchaseRecordDAO;

import java.util.Calendar;
import java.util.List;

/**
 * 进货扫描界面
 */
public class PurchaseInfoActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String BARCODE = "barcode";
    private EditText goodsNameEt;
    private EditText quantityEt;
    private EditText priceEt;

    private MyUser user;

    private Goods goods;
    private GoodsDAO goodsDAO;
    private PurchaseRecordDAO purchaseRecordDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();

        final String barcode = getIntent().getStringExtra(BARCODE);

        goodsDAO = GoodsDAO.getInstance(this);
        purchaseRecordDAO = PurchaseRecordDAO.getInstance(this);

        user = CustomApplication.getUser();

        goodsDAO.query(user.getObjectId(), barcode, new DBCallback<Goods>() {
            @Override
            public void onDBCallbck(List<Goods> datas) {
                goods = datas.get(0);
                if (goods == null) {
                    goods = new Goods();
                    goods.user_id = user.getObjectId();
                    goods.barcode = barcode;
                }
                goodsNameEt.setText(goods.name);
                priceEt.setText(goods.purchase_price + "");
            }
        });

    }

    private void initView(){
        goodsNameEt = (EditText) findViewById(R.id.et_goods_name);
        quantityEt = (EditText) findViewById(R.id.et_counts);
        priceEt = (EditText) findViewById(R.id.et_price);

        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    private void savePurchaseInfo(){
        String goodName = goodsNameEt.getText().toString();
        if (TextUtils.isEmpty(goodName)){
            goodsNameEt.setError(getString(R.string.error_field_required));
            goodsNameEt.requestFocus();
            return;
        }

        String priceStr = priceEt.getText().toString();
        float price = 0;
        if (!TextUtils.isEmpty(priceStr)){
            price = Float.parseFloat(priceStr);
        }else{
            priceEt.setError(getString(R.string.error_field_required));
            priceEt.requestFocus();
            return;
        }

        if (price <= 0){
            priceEt.setError(getString(R.string.error_price_invalidate));
            priceEt.requestFocus();
            return;
        }

        String countStr = quantityEt.getText().toString();
        int quantity = 0;
        if (!TextUtils.isEmpty(countStr)){
            quantity = Integer.parseInt(countStr);
        }else{
            quantityEt.setError(getString(R.string.error_field_required));
            quantityEt.requestFocus();
            return;
        }

        if (quantity <= 0){
            quantityEt.setError(getString(R.string.error_quantity_invalidate));
            quantityEt.requestFocus();
            return;
        }

        PurchaseRecord record = new PurchaseRecord();
        record.user_id = user.getObjectId();
        record.purchase_price = price;
        record.date = Calendar.getInstance().getTime().getTime() + "";
        record.quantity = quantity;

        goods.name = goodName;
        goods.purchase_price = price;
        goods.selling_price = price + 1;
        record.setGoods(goods);

        purchaseRecordDAO.insert(record, new DBUpdateCallback() {
            @Override
            public void onDBUpdate(boolean success) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                savePurchaseInfo();
                break;
        }
    }
}
