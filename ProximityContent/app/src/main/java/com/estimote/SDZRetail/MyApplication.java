package com.estimote.SDZRetail;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;

import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;

import java.util.List;
import java.util.UUID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.*;


//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();
        try{

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
                            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), // Track using all beaons
                            null, null));
                }
            });

            beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
                @Override
                public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {
                    showNotification(
                            "Beacon Notification.",
                            "You're in the range of a beacon.");
                }
                @Override
                public void onExitedRegion(BeaconRegion region) {
                    // could add an "exit" notification too if you want (-:
                }
            });


        }
        catch (Exception e){
            Log.e("Error:", e.getMessage());
        }



        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
        //EstimoteSDK.enableDebugLogging(true);
    }
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}
