package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 20:24
 * Summary: 其他类目的底部list列表
 */
public class CommonListBean {

    /**
     * pages : 1
     * size : 10
     * total : 1
     * current : 1
     * records :
     */

    private int pages;
    private int size;
    private int total;
    private int current;
    private List<RecordsBean> records;

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

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }
}
