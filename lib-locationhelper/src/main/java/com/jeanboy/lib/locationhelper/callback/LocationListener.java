package com.jeanboy.lib.locationhelper.callback;

import com.jeanboy.lib.locationhelper.model.LocationModel;

/**
 * Created by jeanboy on 2017/9/25.
 */

public interface LocationListener {

    void onUpdate(LocationModel locationModel);

    void onNeedPermission();

    void onNeedPositionService();

    void onError();
}
