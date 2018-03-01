package com.estimote.SDZRetail;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        // TODO: put your App ID and App Token here
        // You can get them by adding your app on https://cloud.estimote.com/#/apps
        EstimoteSDK.initialize(getApplicationContext(), "bryan-koivisto-s-proximity-7nl", "2d6516d805a938901e8b63cb9951e9fb");
        EstimoteSDK.enableRangingAnalytics(true);
        EstimoteSDK.enableMonitoringAnalytics(true);

        //Creates beacon region to do background tracking
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion(
                        "monitored region",
                        UUID.fromString("rid"), // Track using all beaons
                        null, null));
            }
        });

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
        //EstimoteSDK.enableDebugLogging(true);
    }


}
