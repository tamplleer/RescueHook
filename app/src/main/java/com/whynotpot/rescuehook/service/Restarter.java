package com.whynotpot.rescuehook.service;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.JOB_SCHEDULER_SERVICE;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import com.whynotpot.rescuehook.FastStartService;

import timber.log.Timber;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("aaaavvvvvvvvvvvvvvvvv");
        final PendingResult pendingResult = goAsync();

        if (intent.getAction().equals("STOP")) {
            intent = new Intent(context, FastStartService.class);
            context.stopService(intent);
            FastStartService.isMyServiceRunning = false;
            Timber.i("aaaaaaaaaaaaaaaaaaaaaaaaa");
        } else {
            intent = new Intent(context, FastStartService.class);
            Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
            Task asyncTask = new Task(pendingResult, intent, context);
            asyncTask.execute();
        }

    }

    private static class Task extends AsyncTask<String, Integer, String> {

        private final PendingResult pendingResult;
        private final Intent intent;
        private final Context context;

        private Task(PendingResult pendingResult, Intent intent, Context context) {
            this.pendingResult = pendingResult;
            this.intent = intent;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String log = "Action: " + intent.getAction() + "\n" +
                    "URI: " + intent.toUri(Intent.URI_INTENT_SCHEME) + "\n";
            Timber.i("Service tried to doInBackground");

            if (Build.BRAND.equals("xiaomi")) {
                intent.putExtra("type", "foreground");
                context.startService(intent);
            } else {
                intent.putExtra("type", "background");
                context.startForegroundService(intent);
            }
            return log;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish();
        }
    }
}