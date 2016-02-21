package com.chiemy.app.cashier.db;

/**
 * 数据库更新回调
 * Created by chiemy on 16/2/21.
 */
public interface DBUpdateCallback {
    /**
     *
     * @param success 是否更新成功
     */
    void onDBUpdate(boolean success);
}
