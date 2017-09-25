package com.jeanboy.lib.locationhelper.net.entity;

/**
 * Created by jeanboy on 2017/5/23.
 */

public class GeoResponseEntity {

    private int result;
    private GeoLocationEntity data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public GeoLocationEntity getData() {
        return data;
    }

    public void setData(GeoLocationEntity data) {
        this.data = data;
    }
}
