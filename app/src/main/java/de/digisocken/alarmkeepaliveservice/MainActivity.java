package de.digisocken.alarmkeepaliveservice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tv = (TextView) findViewById(R.id.textView);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        tv.setText(
                String.format("Service Do: %d\n" +
                        "Alarm to check service: %d\n" +
                        "Service restarts: %d",
                        mPreferences.getInt("countDo", 0),
                        mPreferences.getInt("countAlarm", 0),
                        mPreferences.getInt("countServiceStart", 0)
                )
        );
    }
}
