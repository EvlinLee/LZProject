package com.by.lizhiyoupin.app.component_ui.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * data:2019/12/18
 * author:jyx
 * function: 获取 经纬度
 */
public class LocationManager {
    public static final int LOCATION_CODE = 301;

    private static volatile  LocationManager mLocationManager;


    public static LocationManager getInstance(){
        if (mLocationManager==null){
            mLocationManager=new LocationManager();
        }
        return mLocationManager;

    }



    public double[] getLocationInApp(Context context){
          android.location.LocationManager locationManager;
          String locationProvider = null;
        double[]  location1=new double[2];


        locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(android.location.LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = android.location.LocationManager.NETWORK_PROVIDER;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
            } else {
                //监视地理位置变化

                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location != null) {
                    location1[0]=location.getLatitude();
                    location1[1]=location.getLongitude();
                }
            }
        } else {
            //监视地理位置变化

            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                location1[0]=location.getLatitude();
                location1[1]=location.getLongitude();

            }
        }

        return location1;
    }
}
