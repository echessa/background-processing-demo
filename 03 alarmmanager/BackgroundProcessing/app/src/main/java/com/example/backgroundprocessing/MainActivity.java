package com.example.backgroundprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private AlarmManager manager;
    private PendingIntent pendingIntent;
    private static final int REQ_CODE = 0;
    private String switchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwitchCompat alarmSwitch = findViewById(R.id.alarmSwitch);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        boolean alarmOn = (PendingIntent.getBroadcast(this, REQ_CODE, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);
        alarmSwitch.setChecked(alarmOn);
        switchText = alarmOn ? "Alarm On" : "Alarm Off";
        alarmSwitch.setText(switchText);

        manager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQ_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);
                    switchText = "Alarm On";
                } else {
                    if (pendingIntent == null) {
                        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQ_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }
                    manager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    switchText = "Alarm Off";
                }
                buttonView.setText(switchText);
            }
        });
    }
}