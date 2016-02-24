package com.chiemy.app.cashier.bean;

/**
 * 进货记录
 */
public class PurchaseRecord {
    public String user_id;
    /**
     * 商品id
     */
    private String goods_id;
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

    private Goods goods;

    public void setGoods(Goods goods) {
        this.goods = goods;
        goods_id = goods.barcode;
    }

    public Goods getGoods() {
        return goods;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }
}
