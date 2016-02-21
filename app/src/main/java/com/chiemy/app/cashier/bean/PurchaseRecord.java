package com.chiemy.app.cashier.bean;

/**
 * 进货记录
 */
public class PurchaseRecord {
    public String user_id;
    /**
     * 商品id
     */
    public String goods_id;
    /**
     * 进货数量
     */
    public int quantity;
    /**
     * 供应商
     */
    public String supplier;

    /**
     * 进货日期
     */
    public String date;
    /**
     * 备注
     */
    public String remarks;

    /**
     * 进货价格
     */
    public float purchase_price;
}
