package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 19:52
 * Summary: tabLayout的title+非精选的banner+icon
 */
public class CommonSecondBean {
    private List<CommonCategoryBean> banner;
    private List<CommonCategoryBean> second;

    public List<CommonCategoryBean> getBanner() {
        return banner;
    }

    public void setBanner(List<CommonCategoryBean> banner) {
        this.banner = banner;
    }

    public List<CommonCategoryBean> getSecond() {
        return second;
    }

    public void setSecond(List<CommonCategoryBean> second) {
        this.second = second;
    }
}
