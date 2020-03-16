package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/6 10:20
 * Summary:
 */
public class SelectPicBean {
    private String url;
    private boolean selected;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SelectPicBean{" +
                "url='" + url + '\'' +
                ", selected=" + selected +
                '}';
    }
}
