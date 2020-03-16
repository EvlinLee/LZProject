package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/24 15:21
 * Summary:
 */
public class PreciseStickEntity {
    private String title;
    private String tip;
    private boolean selected;


    public PreciseStickEntity(String title, String tip, boolean selected) {
        this.title = title;
        this.tip = tip;
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
