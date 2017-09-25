package com.jeanboy.lib.locationhelper.net.base;

/**
 * Created by Next on 2016/7/13.
 */
public interface RequestCallback<R> {

    void onSuccess(R response);

    void onError(int code, String msg);
}
