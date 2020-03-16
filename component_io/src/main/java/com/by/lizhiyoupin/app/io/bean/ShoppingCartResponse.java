package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 17:25
 * Summary:
 */
public class ShoppingCartResponse {
    private int pages;//页数
    private int size;//一页数量
    private int total;//总数量
    private int current;//当前页
    private List<ShoppingCartListBean> records;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<ShoppingCartListBean> getRecords() {
        return records;
    }

    public void setRecords(List<ShoppingCartListBean> records) {
        this.records = records;
    }
}
