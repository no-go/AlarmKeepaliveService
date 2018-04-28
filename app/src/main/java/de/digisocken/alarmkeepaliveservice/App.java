package de.digisocken.alarmkeepaliveservice;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    public static KeepaliveAlarm alarm = null;
    public static Context contextOfApplication;
    public static boolean mBound = false;

    @Override
    public void onCreate() {
        super.onCreate();
        contextOfApplication = getApplicationContext();
        if (alarm == null) alarm = new KeepaliveAlarm();
        alarm.set(contextOfApplication);
    }
}
