package com.jeanboy.lib.locationhelper.features;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import com.jeanboy.lib.locationhelper.callback.LocationCallback;
import com.jeanboy.lib.locationhelper.model.LocationModel;
import com.jeanboy.lib.locationhelper.net.api.LocationApiDao;
import com.jeanboy.lib.locationhelper.net.base.RequestCallback;
import com.jeanboy.lib.locationhelper.net.base.RequestParams;
import com.jeanboy.lib.locationhelper.net.base.ResponseData;
import com.jeanboy.lib.locationhelper.net.entity.GeoResponseEntity;
import com.jeanboy.lib.locationhelper.net.manager.OkHttpManager;

import retrofit2.Call;

/**
 * Created by jeanboy on 2017/5/23.
 */

public class MylnikovLocaiton {

    /**
     * https://www.mylnikov.org/archives/1056
     * <p>
     * 通过公共移动电话位置数据库
     *
     * @param context
     * @param callback
     */
    public static void getLocation(Context context, final LocationCallback callback) {

        if (context == null || callback == null) return;

        try {
            final Context applicationContext = context.getApplicationContext();
            TelephonyManager manager = (TelephonyManager) applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
            String operator = manager.getNetworkOperator();
            int mcc = Integer.parseInt(operator.substring(0, 3));
            int mnc = Integer.parseInt(operator.substring(3));
            GsmCellLocation location = (GsmCellLocation) manager.getCellLocation();
            int lac = location.getLac();
            int cellId = location.getCid();
            String key = String.valueOf(mcc) + String.valueOf(mnc) + String.valueOf(lac) + String.valueOf(cellId);

            LocationApiDao locationApiDao = OkHttpManager.getInstance().create(LocationApiDao.BASE_URL, LocationApiDao.class);
            Call<GeoResponseEntity> call = locationApiDao.getGeoLocation(mcc, mnc, lac, cellId, LocationApiDao.API_SERVICE_VERSION);
            OkHttpManager.getInstance().doBack(new RequestParams<>(call), new RequestCallback<ResponseData<GeoResponseEntity>>() {
                @Override
                public void onSuccess(ResponseData<GeoResponseEntity> response) {
                    if (response == null) {
                        getLocationByWifi(applicationContext, callback);
                        return;
                    }

                    GeoResponseEntity responseBody = response.getBody();

                    if (responseBody == null) {
                        getLocationByWifi(applicationContext, callback);
                        return;
                    }

                    if (responseBody.getData() == null) {
                        getLocationByWifi(applicationContext, callback);
                        return;
                    }

                    LocationModel locationModel = new LocationModel(responseBody.getData().getLon(),
                            responseBody.getData().getLat());
                    callback.onUpdate(locationModel);
                }

                @Override
                public void onError(int code, String msg) {
                    getLocationByWifi(applicationContext, callback);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError();
        }
    }

    /**
     * https://www.mylnikov.org/
     * <p>
     * 通过Wi-Fi（MAC，BSSID）定位
     *
     * @param context
     * @param callback
     */
    private static void getLocationByWifi(Context context, final LocationCallback callback) {

        if (context == null || callback == null) return;

        Context applicationContext = context.getApplicationContext();

        WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            callback.onError();
            return;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String netMac = wifiInfo.getBSSID(); //获取被连接网络的mac地址

        if (TextUtils.isEmpty(netMac) || netMac.length() < 10) {
            callback.onError();
            return;
        }

        LocationApiDao locationApiDao = OkHttpManager.getInstance().create(LocationApiDao.BASE_URL, LocationApiDao.class);
        Call<GeoResponseEntity> call = locationApiDao.getWifiLocation(netMac, LocationApiDao.API_SERVICE_VERSION);
        OkHttpManager.getInstance().doBack(new RequestParams<>(call), new RequestCallback<ResponseData<GeoResponseEntity>>() {
            @Override
            public void onSuccess(ResponseData<GeoResponseEntity> response) {
                if (response == null) {
                    callback.onError();
                    return;
                }
                GeoResponseEntity responseBody = response.getBody();

                if (responseBody == null) {
                    callback.onError();
                    return;
                }

                if (responseBody.getData() == null) {
                    callback.onError();
                    return;
                }

                LocationModel locationModel = new LocationModel(responseBody.getData().getLon(),
                        responseBody.getData().getLat());
                callback.onUpdate(locationModel);
            }

            @Override
            public void onError(int code, String msg) {
                callback.onError();
            }
        });
    }
}
