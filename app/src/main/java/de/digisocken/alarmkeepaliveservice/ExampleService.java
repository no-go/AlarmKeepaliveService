package de.digisocken.alarmkeepaliveservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class ExampleService  extends Service {
    public static final String NOTIFICATION_CHANNEL_ID_LOCATION = "de_digisocken_alarmkeepaliveservice_exampleservice";

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
            showNotification("Service is doing stuff " + String.valueOf(doing+1));
            handler.postDelayed(this, 30000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        handler.postDelayed(exampleServiceThread, 30000);
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

    private void showNotification(String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager mngr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (mngr.getNotificationChannel(NOTIFICATION_CHANNEL_ID_LOCATION) != null) {
                mngr.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID_LOCATION);
            }
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID_LOCATION,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT // low = sound off in 8 and higher
            );
            channel.setDescription(getString(R.string.app_name));
            channel.enableLights(true);
            channel.enableVibration(false);
            mngr.createNotificationChannel(channel);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText(text);

        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_LOCATION)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setLargeIcon(largeIcon)
                .setStyle(bigStyle)
                .setSound(sound)
                .setLights(Color.CYAN, 2000, 500)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // LOW = sound off in 7.1 and below
                .setTicker(text)  // the status text
                .setWhen(Calendar.getInstance().getTimeInMillis())  // the time stamp
                .setContentTitle(getString(R.string.app_name))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                //.setOngoing(true)                 // remove/only cancel by stop button
                .build();
        mNM.notify((int) System.currentTimeMillis(), notification);
    }
}
