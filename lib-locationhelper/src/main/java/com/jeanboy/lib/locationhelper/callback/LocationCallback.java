package com.jeanboy.lib.locationhelper.callback;


import com.jeanboy.lib.locationhelper.model.LocationModel;

/**
 * Created by jeanboy on 2017/5/23.
 */

public interface LocationCallback {

    void onUpdate(LocationModel location);

    void onError();

}
