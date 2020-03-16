package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 12:00
 * Summary:
 */
public class VipGoodListBean {
    private int pages; //页数
    private int size;//一页数量
    private int total;//总数量
    private int current;//当前页
    private List<VipGoodsBean> records;

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

    public List<VipGoodsBean> getRecords() {
        return records;
    }

    public void setRecords(List<VipGoodsBean> records) {
        this.records = records;
    }
}
