package com.jeanboy.lib.locationhelper.net.base;

/**
 * Created by jeanboy on 2017/5/17.
 */

public class RequestParams<T> {

    private T params;

    public RequestParams(T body) {
        this.params = body;
    }

    public T getParams() {
        return params;
    }
}
