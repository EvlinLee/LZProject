package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/8 16:49
 * Summary:
 */
public class JS2NativeEntity<K,V>   {
   double location_x;
   double location_y;

    public JS2NativeEntity(double location_x, double location_y) {
        this.location_x = location_x;
        this.location_y = location_y;
    }

    public double getLocation_x() {
        return location_x;
    }

    public void setLocation_x(double location_x) {
        this.location_x = location_x;
    }

    public double getLocation_y() {
        return location_y;
    }

    public void setLocation_y(double location_y) {
        this.location_y = location_y;
    }
}
