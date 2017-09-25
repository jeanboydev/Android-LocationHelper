package com.jeanboy.lib.locationhelper;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.jeanboy.lib.locationhelper.callback.LocationCallback;
import com.jeanboy.lib.locationhelper.callback.LocationListener;
import com.jeanboy.lib.locationhelper.features.MyAndroidLocation;
import com.jeanboy.lib.locationhelper.features.MylnikovLocaiton;
import com.jeanboy.lib.locationhelper.model.LocationModel;


/**
 * Created by jeanboy on 2017/5/23.
 */

public class MyLocationHelper {

    /**
     * 获取位置入口
     *
     * @param context
     * @param listener
     */
    public static void requestLocation(final Context context, final LocationListener listener) {
        if (context == null || listener == null) return;

        final Context applicationContext = context.getApplicationContext();

        if (MyAndroidLocation.checkSelfPermission(applicationContext)) {
            listener.onNeedPermission();
            return;
        }

        MyAndroidLocation.getLocation(applicationContext, new LocationCallback() {
            @Override
            public void onUpdate(LocationModel location) {
                Log.e(MyLocationHelper.class.getName(), "======定位成功===Android SDK===");
                listener.onUpdate(location);
            }

            @Override
            public void onError() {
                Log.e(MyLocationHelper.class.getName(), "======定位失败===Android SDK===");
                getLocationByLnikov(applicationContext, listener);
            }
        });
    }

    private static void getLocationByLnikov(final Context context, final LocationListener listener) {
        MylnikovLocaiton.getLocation(context, new LocationCallback() {
            @Override
            public void onUpdate(LocationModel location) {
                Log.e(MyLocationHelper.class.getName(), "======定位成功===Mylnikov===");
                listener.onUpdate(location);
            }

            @Override
            public void onError() {
                Log.e(MyLocationHelper.class.getName(), "======定位失败===Mylnikov===");
                if (!MyAndroidLocation.isLocationSettingsOpened(context)) {
                    listener.onNeedPositionService();
                } else {
                    listener.onError();
                }
            }
        });
    }


    /**
     * 跳转至打开定位页面
     *
     * @return
     */
    public static Intent getToLocationActionIntent() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
