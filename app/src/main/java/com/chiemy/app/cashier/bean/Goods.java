package com.chiemy.app.cashier.bean;

import cn.bmob.v3.BmobObject;

/**
 * 商品
 */
public class Goods extends BmobObject {
    public String user_id;
    /**
     * 条码
     */
    public String barcode;
    /**
     * 名称
     */
    public String name;
    /**
     * 供应商
     */
    public String supplier;
    /**
     * 商品进价
     */
    public float purchase_price;
    /**
     * 商品售价
     */
    public float selling_price;
    /**
     * 上传状态
     */
    public int update_state; // 0-未上传，1-已上传
    /**
     * 商品数量
     */
    public int quantity;
}
