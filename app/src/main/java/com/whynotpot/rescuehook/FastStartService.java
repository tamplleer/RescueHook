package com.whynotpot.rescuehook;

import static com.whynotpot.rescuehook.screens.main.MainActivity.NEW_RESTART_SERVICE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.whynotpot.rescuehook.common.Constants;
import com.whynotpot.rescuehook.floatButton.FastStartButton;
import com.whynotpot.rescuehook.floatButton.SimpleFloatButton;
import com.whynotpot.rescuehook.screens.main.MainActivity;
import com.whynotpot.rescuehook.service.OverScreenService;
import com.whynotpot.rescuehook.service.Restarter;
import com.whynotpot.rescuehook.service.TimerExecutable;
import com.whynotpot.rescuehook.themes.CallBack;
import com.whynotpot.rescuehook.themes.Theme;
import com.whynotpot.rescuehook.themes.ThemeFastStart;
import com.whynotpot.rescuehook.themes.ThemeSimpleAlpha;

import timber.log.Timber;

public class FastStartService extends Service implements CallBack {
    private WindowManager windowManager;
    private Theme theme;
    private FastStartButton button;
    private int startId;
    public static boolean isMyServiceRunning = false;
    private boolean jobCanceled = false;

    public FastStartService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("type").equals("foreground")) {
            startMyOwnForeground();
        }
        ;
        button.getFloatingFaceBubble().setOnClickListener(view -> {
            Toast.makeText(this, "Сдшсл",
                    Toast.LENGTH_SHORT).show();
            stopSelf();
        });
        this.startId = startId;
        return START_STICKY;
    }

/*    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setPackage("com.whynotpot.servicetest");
        sendBroadcast(broadcastIntent);
        Timber.i("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyeeeeeeeeeeeeeeeeeeeeessssssss");

        // Handle application closing
        Toast.makeText(this, "Сдшсл",
                Toast.LENGTH_SHORT).show();
     *//*   Intent broadcastIntent = new Intent();
        broadcastIntent.setPackage("com.whynotpot.rescuehook");
        broadcastIntent.setAction(NEW_RESTART_SERVICE);*//*
        // Destroy the service
        stopSelf();
    }*/

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
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

/*    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Timber.i("Job started");
      new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Timber.i("count = " + i);
                if (jobCanceled){
                    return;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Timber.i("FINISHED JOB");
            jobFinished(jobParameters, false);
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Timber.i("Job stopped");
        jobCanceled = true;

        return true;
    }*/

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //startMyOwnForeground();
        theme = new ThemeFastStart(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        theme.onCreate(inflater);
        button = new FastStartButton(this, R.drawable.finger, windowManager, theme);

        //   windowManager.addView(theme.getView(), theme.getParams());
        windowManager.addView(button.getFloatingFaceBubble(), button.getMyParams());


    }

    private void openService() {
        Intent intent;
        intent =
                new Intent(this, OverScreenService.class)
                        .putExtra(Constants.THEME_INTENT, "alpha")
                        .putExtra(Constants.TIME, 60000);
        startService(intent);
    }

    @Override
    public void onDestroy() {
        Timber.i("stop3");
        Toast.makeText(this, "Служба быстрого старта остановлена",
                Toast.LENGTH_SHORT).show();
        if (button.getFloatingFaceBubble() != null) {
            windowManager.removeViewImmediate(button.getFloatingFaceBubble());
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setPackage("com.whynotpot.rescuehook");
        broadcastIntent.setAction(NEW_RESTART_SERVICE);
        //   broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    /*    if (theme.getView() != null) {
            windowManager.removeViewImmediate(theme.getView());
        }*/


    }

    @Override
    public void callbackCall() {
        openService();
        windowManager.removeViewImmediate(theme.getView());
        button = new FastStartButton(this, R.drawable.finger, windowManager, theme);
        windowManager.addView(button.getFloatingFaceBubble(), button.getMyParams());
    }

    public class MyBackgroundServiceBinder extends Binder {
        public FastStartService getService() {
            return FastStartService.this;
        }
    }
}

