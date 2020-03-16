package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/29 15:24
 * Summary:
 */
public class RequestDetailPicEntity {
    private String id;
    private String type;

    public RequestDetailPicEntity() {
    }

    public RequestDetailPicEntity(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RequestDetailPicEntity{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
