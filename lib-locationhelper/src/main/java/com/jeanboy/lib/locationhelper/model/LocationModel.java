package com.jeanboy.lib.locationhelper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeanboy on 2017/5/17.
 */

public class LocationModel implements Parcelable {

    private double longitude;//经度
    private double latitude;//维度
    private long updateTime;

    public LocationModel() {
    }

    public LocationModel(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.updateTime = System.currentTimeMillis();
    }

    protected LocationModel(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
        updateTime = in.readLong();
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "LocationModel{" + "longitude=" + longitude + ", latitude=" + latitude + ", updateTime=" + updateTime + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeLong(updateTime);
    }
}
