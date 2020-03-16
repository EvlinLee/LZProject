package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/30 20:42
 * Summary:
 */
public class SearchTaoBean {


    /**
     * itemId : 573545979107
     * pictUrl : https://img.alicdn.com/bao/uploaded/i3/2883881152/O1CN01ZZni9t1KNemnrl2c7_!!0-item_pic.jpg
     * title : 袜子男中筒袜长袜潮秋冬季男士潮流高帮潮牌长筒篮球袜男袜棉运动
     */

    private Long itemId;
    private String pictUrl;
    private String title;
    private String oldText;//native 添加的原始数据

    public SearchTaoBean() {
    }

    public SearchTaoBean(String title, String oldText) {
        this.title = title;
        this.oldText = oldText;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getPictUrl() {
        return pictUrl;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOldText() {
        return oldText;
    }

    public void setOldText(String oldText) {
        this.oldText = oldText;
    }
}
