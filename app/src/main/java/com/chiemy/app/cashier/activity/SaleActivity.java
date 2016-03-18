package com.chiemy.app.cashier.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiemy.app.cashier.R;
import com.chiemy.zxing.activity.CaptureActivity;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * 售货
 */
public class SaleActivity extends CaptureActivity {
    private List<String> datas;
    private SaleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datas = new ArrayList<>(10);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_sale);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SaleAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sale;
    }

    @Override
    protected void onDecode(Result rawResult, Bundle bundle) {
        restartPreviewAfterDelay(2000);
        datas.add(rawResult.getText());
        adapter.notifyDataSetChanged();
    }

    private class SaleAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private LayoutInflater inflater;

        public SaleAdapter() {
            inflater = LayoutInflater.from(SaleActivity.this);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setTextColor(Color.WHITE);
        }
    }

}
