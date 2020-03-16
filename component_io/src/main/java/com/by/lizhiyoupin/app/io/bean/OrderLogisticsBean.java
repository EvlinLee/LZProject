package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * data:2020/1/10
 * author:jyx
 * function:
 */
public class OrderLogisticsBean {
    private String LogisticCode;
    private String ShipperCode;
    private String ShipperName;
    private String ShipperPhone;
    private String ShipperImg;

    private List<TracesBean> Traces;

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public String getShipperPhone() {
        return ShipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        ShipperPhone = shipperPhone;
    }

    public String getShipperImg() {
        return ShipperImg;
    }

    public void setShipperImg(String shipperImg) {
        ShipperImg = shipperImg;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public List<TracesBean> getTraces() {
        return Traces;
    }

    public void setTraces(List<TracesBean> traces) {
        Traces = traces;
    }
}
