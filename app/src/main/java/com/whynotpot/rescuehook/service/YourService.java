package com.whynotpot.rescuehook.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.floatButton.FastStartButton;
import com.whynotpot.rescuehook.themes.CallBack;
import com.whynotpot.rescuehook.themes.Theme;
import com.whynotpot.rescuehook.themes.ThemeFastStart;

import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class YourService extends JobIntentService implements CallBack {
    public int counter = 0;
    private WindowManager windowManager;
    private FastStartButton button;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.i("assssssssssssssssssssssssssssssssssssssssss");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
      //  startMyOwnForeground();
        Theme theme = new ThemeFastStart(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        theme.onCreate(inflater);
        button = new FastStartButton(this, R.drawable.finger, windowManager, theme);

        //   windowManager.addView(theme.getView(), theme.getParams());
        windowManager.addView(button.getFloatingFaceBubble(), button.getMyParams());
    }


    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Timber.i("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
        Toast.makeText(this, "Key",
                Toast.LENGTH_SHORT).show();
        startTimer();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }


    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Log.i("Count", "=========  " + (counter++));
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void callbackCall() {

    }
}