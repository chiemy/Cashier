package com.chiemy.app.cashier.bean;

import cn.bmob.v3.BmobObject;

/**
 * 销售记录
 */
public class SaleRecord extends BmobObject {
    public String user_id;
    /**
     * 销售的商品标识
     */
    public String goods_id;
    /**
     * 销售商品的数量
     */
    public int count;
    /**
     * 销售日期
     */
    public String date;

    /**
     * 备注
     */
    public String remarks;

    /**
     * 销售价格
     */
    public float selling_price;
}
