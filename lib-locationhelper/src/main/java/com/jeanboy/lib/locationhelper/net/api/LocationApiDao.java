package com.jeanboy.lib.locationhelper.net.api;


import com.jeanboy.lib.locationhelper.net.entity.GeoResponseEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * https://www.mylnikov.org/
 * <p>
 * Created by jeanboy on 2017/5/23.
 */

public interface LocationApiDao {


    String BASE_URL = "https://api.mylnikov.org";

    String API_SERVICE_VERSION = "1.1";


    @GET("/geolocation/wifi")
    Call<GeoResponseEntity> getWifiLocation(@Query("bssid") String id, @Query("v") String version);

    @GET("/geolocation/cell")
    Call<GeoResponseEntity> getGeoLocation(@Query("mcc") int mcc, @Query("mnc") int mnc, @Query("lac") int lac, @Query
            ("cellid") int cellId, @Query("v") String version);
}
