package de.digisocken.alarmkeepaliveservice;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class ExampleService  extends Service {
    private final IBinder mBinder = new ExampleBinder();

    public class ExampleBinder extends Binder {
        ExampleService getService() {
            return ExampleService.this;
        }
    }

    private Handler handler = new Handler();
    private final Runnable exampleServiceThread = new Runnable() {
        public void run() {
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(App.contextOfApplication);
            int doing = mPreferences.getInt("countDo", 0);
            mPreferences.edit().putInt("countDo", doing+1).apply();
            Log.v(getClass().getSimpleName(), "Service is doing stuff");
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        handler.postDelayed(exampleServiceThread, 1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(App.contextOfApplication);
        int starts = mPreferences.getInt("countServiceStart", 0);
        mPreferences.edit().putInt("countServiceStart", starts+1).apply();
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
