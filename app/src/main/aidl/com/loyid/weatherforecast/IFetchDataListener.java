// IFetchDataListener.aidl
package com.loyid.weatherforecast;

// Declare any non-default types here with import statements

public interface IFetchDataListener {
    void onWeatherDataRetrieved(String[] data);
}