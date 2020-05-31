// IFetchWeatherService.aidl
package com.loyid.weatherforecast;

// Declare any non-default types here with import statements

public interface IFetchWeatherService {
    void retrieveWeatherData();
    void registerFetchDataListener(IFetchDataListener listener);
    void unregisterFetchDataListener(IFetchDataListener listener);
}