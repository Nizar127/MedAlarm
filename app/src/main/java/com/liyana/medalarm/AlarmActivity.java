package com.liyana.medalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    ToggleButton btnToggle;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        backBtn = findViewById(R.id.backhomebtn);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        btnToggle = findViewById(R.id.toggleButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               long time;
               if(((ToggleButton) v).isChecked()){
                   Toast.makeText(AlarmActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
                   Calendar calendar = Calendar.getInstance();

                   // calender is called to get current time in hour and minute
                   calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                   calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                   Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);

                   // we call broadcast using pendingIntent
                   pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this,0,intent,0);

                   time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis()) % 60000);
                    if(System.currentTimeMillis() > time){
                        //setting tiem
                        if(calendar.AM_PM == 0){
                            time = time + (1000 * 60 * 60 * 12);
                        }else{
                            time = time = (1000 * 60 * 60 * 24);
                        }

                        // Alarm rings continuously until toggle button is turned off
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
                        // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);

                    }else {
                        alarmManager.cancel(pendingIntent);
                        Toast.makeText(AlarmActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();


                    }


               }

            }
        });
    }




}