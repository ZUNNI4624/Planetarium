package com.zunairah.planetarium.data.api;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(Exception e);
}
