package com.jeanboy.lib.locationhelper.net.entity;

/**
 * Created by jeanboy on 2017/5/23.
 */

public class GeoLocationEntity {

    private float lat;
    private float lon;
    private float range;
    private long time;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
