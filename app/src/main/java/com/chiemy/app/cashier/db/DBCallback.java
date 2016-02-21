package com.chiemy.app.cashier.db;

import java.util.List;

/**
 * 数据库查询回调
 */
public interface DBCallback<T> {
    /**
     * 回调
     */
    void onDBCallbck(List<T> datas);
}
