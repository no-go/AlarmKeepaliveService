package de.digisocken.alarmkeepaliveservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (App.alarm == null) App.alarm = new KeepaliveAlarm();
            App.alarm.set(context);
        }
    }
}
