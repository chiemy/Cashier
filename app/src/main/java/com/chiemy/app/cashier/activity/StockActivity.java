package com.chiemy.app.cashier.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiemy.app.cashier.CustomApplication;
import com.chiemy.app.cashier.R;
import com.chiemy.app.cashier.bean.Goods;
import com.chiemy.app.cashier.db.DBCallback;
import com.chiemy.app.cashier.db.GoodsDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存界面
 */
public class StockActivity extends AppCompatActivity {
    private GoodsDAO goodsDAO;
    private List<Goods> goodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goodsList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_goods_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final GoodsListAdapter adapter = new GoodsListAdapter();
        recyclerView.setAdapter(adapter);

        goodsDAO = GoodsDAO.getInstance(this);
        goodsDAO.queryAll(CustomApplication.getUser().getObjectId(), new DBCallback<Goods>() {
            @Override
            public void onDBCallbck(List<Goods> datas) {
                goodsList.addAll(datas);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private class GoodsListAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private LayoutInflater inflater;

        public GoodsListAdapter() {
            inflater = LayoutInflater.from(StockActivity.this);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.item_goods_list, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(goodsList.get(position).name);
        }

        @Override
        public int getItemCount() {
            return goodsList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_text);
            tv.setTextColor(Color.WHITE);
        }
    }

}
