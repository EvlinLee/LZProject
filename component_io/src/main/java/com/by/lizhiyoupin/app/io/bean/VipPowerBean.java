package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/22 17:44
 * Summary:
 */
public class VipPowerBean {
    /**
     * id : 1
     * name : 名称
     * icon : //gw.alicdn.com/bao/uploaded/i4/3252696262/O1CN01HURIZV1w82eDxlucp_!!3252696262.jpg
     * url : //gw.alicdn.com/bao/uploaded/i4/3252696262/O1CN01HURIZV1w82eDxlucp_!!3252696262.jpg
     * level : 2
     */

    private long id;//主键id
    private String name;//权益名称
    private String icon;//图标
    private String url;//链接
    private int level;//角色等级要求 1-普通 2-超级 3-Plus超级 4-运营商 5-plus运营商
    private int drawRes;//netive自己添加的，本地图片资源id
    private boolean nativeDraw;//是否使用 drawRes作为图片地址

    public boolean isNativeDraw() {
        return nativeDraw;
    }

    public void setNativeDraw(boolean nativeDraw) {
        this.nativeDraw = nativeDraw;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getDrawRes() {
        return drawRes;
    }

    public void setDrawRes(int drawRes) {
        this.drawRes = drawRes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
