package com.whynotpot.rescuehook.service;

import static com.whynotpot.rescuehook.screens.main.MainActivity.NEW_RESTART_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.whynotpot.rescuehook.FastStartService;

import timber.log.Timber;

public class BootReceiver extends BroadcastReceiver {
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("BootReceiver");
        mContext = context;
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String message = "Обнаружено сообщение "
                    + intent.getAction();

            Toast.makeText(context, message,
                    Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(context, FastStartService.class);
            context.startService(serviceIntent);
        }
        if (intent.getAction().equals(NEW_RESTART_SERVICE)) {
            String message = "Обнаружено сообщение "
                    + intent.getAction();

            Toast.makeText(context, message,
                    Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(context, FastStartService.class);
            context.startService(serviceIntent);
        }
    }
}