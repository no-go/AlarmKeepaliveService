package de.digisocken.alarmkeepaliveservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class KeepaliveAlarm extends BroadcastReceiver {

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ExampleService.ExampleBinder binder = (ExampleService.ExampleBinder) iBinder;
            binder.getService();
            App.mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            App.mBound = false;
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getSimpleName(), "Alarm is waked up.");
        // is service not running?
        if (!App.mBound) {
            Intent alarmIntent = new Intent(App.contextOfApplication, ExampleService.class);
            App.contextOfApplication.bindService(alarmIntent, mConnection, Context.BIND_AUTO_CREATE);
            Log.d(getClass().getSimpleName(), "Alarm is (re)bind service");
        } else {
            Log.d(getClass().getSimpleName(), "Service is bounded and running(?)");
        }

        // Count Alarms, if you want
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int cnt = mPreferences.getInt("countAlarm", 0);
        mPreferences.edit().putInt("countAlarm", cnt+1).apply();
    }

    public void set(Context context) {
        AlarmManager am = (AlarmManager) App.contextOfApplication.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(App.contextOfApplication, KeepaliveAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(App.contextOfApplication, 0, i, 0);

        if (am != null) {
            stop(context); // remove old alarms
            am.setInexactRepeating (
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    500L,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    pi
            );
            Log.d(getClass().getSimpleName(), "Alarm is set.");
        } else {
            Log.e(getClass().getSimpleName(), "set: Alarm manager failed.");
        }
    }

    public void stop(Context context) {
        Intent i = new Intent(App.contextOfApplication, KeepaliveAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(App.contextOfApplication, 0, i, 0);
        AlarmManager am = (AlarmManager) App.contextOfApplication.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            Log.d(getClass().getSimpleName(), "Alarm stopped.");
            am.cancel(pi);
        } else {
            Log.e(getClass().getSimpleName(), "stop: Alarm manager failed.");
        }
    }
}
