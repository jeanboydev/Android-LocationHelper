package com.jeanboy.lib.locationhelper.features;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.jeanboy.lib.locationhelper.callback.LocationCallback;
import com.jeanboy.lib.locationhelper.model.LocationModel;


/**
 * Created by jeanboy on 2017/5/17.
 */

public class MyAndroidLocation {

    /**
     * 判断是否打开定位服务
     *
     * @param context
     * @return
     */
    public static boolean isLocationSettingsOpened(Context context) {
        try {
            if (context == null) return false;
            Context applicationContext = context.getApplicationContext();
            LocationManager locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void getLocation(Context context, LocationCallback callback) {
        if (context == null || callback == null) return;
        try {
            Context applicationContext = context.getApplicationContext();

            if (!isLocationSettingsOpened(applicationContext)) {//没有开启定位权限
                callback.onError();
                return;
            }

            LocationManager locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
            Location location = getLocationFromGPS(applicationContext, locationManager);

            if (location == null) {
                location = getLocationFromNetWork(applicationContext, locationManager);
            }

            if (location == null) {
                requestLocationByNetWork(context, locationManager, callback);
            }

            if (location == null) {
                location = getLocationFromPassive(applicationContext, locationManager);
            }

            if (location != null) {
                callback.onUpdate(new LocationModel(location.getLongitude(), location.getLatitude()));
            } else {
                callback.onError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError();
        }
    }

    /**
     * 检查权限
     *
     * @param context
     * @return true 没有权限
     */
    public static boolean checkSelfPermission(Context context) {
        if (context == null) return true;
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 通过 GPS 获取位置
     *
     * @param context
     * @param locationManager
     * @return
     */
    private static Location getLocationFromGPS(Context context, LocationManager locationManager) {
        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//已开启 GPS
            if (checkSelfPermission(context)) {
                //没有权限
                return null;
            }
            //获取到 GPS 位置
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
    }

    /**
     * 通过网络获取位置
     *
     * @param context
     * @param locationManager
     * @return
     */
    private static Location getLocationFromNetWork(Context context, LocationManager locationManager) {
        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (checkSelfPermission(context)) {
                //没有权限
                return null;
            }
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    /**
     * 被动定位方式（例如：百度，高德 SDK）
     *
     * @param context
     * @param locationManager
     * @return
     */
    private static Location getLocationFromPassive(Context context, LocationManager locationManager) {
        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            if (checkSelfPermission(context)) {
                //没有权限
                return null;
            }
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        return location;
    }

    /**
     * 网络定位回调
     *
     * @param context
     * @param locationManager
     * @param callback
     */
    private static void requestLocationByNetWork(Context context, final LocationManager locationManager,
                                                 final LocationCallback callback) {
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (checkSelfPermission(context)) {
                //没有权限
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null && callback != null) {
                        callback.onUpdate(new LocationModel(location.getLongitude(), location.getLatitude()));
                    }
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
    }
}
