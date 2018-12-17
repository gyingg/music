package com.future_melody.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Author WZL
 * Date：2018/6/23 42
 * Notes:
 */
public class TimerServices extends Service {

    //take times from each button, convert them to the milliseconds and addup, then throw into the COunter
    long TIME_LIMIT;
    //countdowntimer object
    CountDownTimer Count;

    TextView textTimer;


    //Receive intent here, onStartCOmmand takes it as a parameter !!!!
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        //Reveive intent here
        Bundle bundle = intent.getExtras();
        TIME_LIMIT = bundle.getLong("Sum_milliseconds");

        Toast.makeText(TimerServices.this, "Service Started", Toast.LENGTH_LONG).show();

        //milisecs in the future, time interval (1 sec)
        Count = new CountDownTimer(TIME_LIMIT, 1000) {

            public void onTick(long millisUntilFinished) {
                //converts miliseconds into seconds
                long seconds = millisUntilFinished / 1000;  //300,000 / 1000 = 300 seconds
                //just add function that automatically transforms milis to hours and minutes

                String time = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                //create intent
                Intent i = new Intent("COUNTDOWN_UPDATED");
                //data added to the intent, name of the data, and the data itself
                i.putExtra("countdown", time);

                //send intent back to the activity
                sendBroadcast(i);
                //coundownTimer.setTitle(millisUntilFinished / 1000);


            }

            public void onFinish() {
                //coundownTimer.setTitle("Sedned!");
                Intent i = new Intent("COUNTDOWN_UPDATED");
                i.putExtra("countdown", "Sent!");

                sendBroadcast(i);
                //Log.d("COUNTDOWN", "FINISH!");

                //stop the service from within itself
                stopSelf();

            }
        };

        Count.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(TimerServices.this, "Service Stopped", Toast.LENGTH_LONG).show();
        Count.cancel();
        super.onDestroy();
    }
}
