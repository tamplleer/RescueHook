package com.whynotpot.rescuehook.service;

import android.annotation.SuppressLint;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.common.Constants;
import com.whynotpot.rescuehook.common.ScreenNavigator;
import com.whynotpot.rescuehook.floatButton.SimpleFloatButton;
import com.whynotpot.rescuehook.screens.result.ResultParams;
import com.whynotpot.rescuehook.themes.Theme;
import com.whynotpot.rescuehook.themes.ThemeSimpleAlpha;
import com.whynotpot.rescuehook.themes.ThemeSimpleAlphaPicture;

import java.util.HashMap;
import java.util.Map;

public class OverScreenService extends Service {
    private WindowManager windowManager;
    private Theme theme;
    private SimpleFloatButton button;
    private TimerExecutable timerExecutable;
    private ScreenNavigator mScreenNavigator;
    private ResultParams resultParams;


    private Map<String, Theme> themeMap = new HashMap<>();

    private void addMapItem() {
        themeMap.put("alpha_text", new ThemeSimpleAlpha());
        themeMap.put("alpha_pic", new ThemeSimpleAlphaPicture());
    }

    private Theme getTheme(String key) {
        return themeMap.getOrDefault(key, new ThemeSimpleAlpha());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        addMapItem();
        // PendingIntent pendingIntent = intent.getParcelableExtra(Constants.PENDING_INTENT);
        int time = intent.getIntExtra(Constants.TIME, 5000);
        theme = getTheme(intent.getStringExtra(Constants.THEME_INTENT));
        //theme.onStartCommand(pendingIntent); todo нужно вообще?
        // startTimer(time);
        timerExecutable = new TimerExecutable(theme, time, windowManager);
        Intent intents = new Intent().putExtra("cat", "Cat");
        LayoutInflater inflater = LayoutInflater.from(this);
        theme.onCreate(inflater);


        windowManager.addView(theme.getView(), theme.getParams());
        windowManager.addView(button.getFloatingFaceBubble(), button.getMyParams());


        button.getFloatingFaceBubble().setOnClickListener(view -> {
            resultParams = new ResultParams(timerExecutable.getRemainingTime());
            //     pendingIntent.send(this, Constants.SERVICE_OVER_SCREEN_REQUEST_CODE, intents);
            stopSelfResult(startId);
        });

        return super.onStartCommand(intent, flags, startId);

    }


    @SuppressLint("ClickableViewAccessibility")
    public void onCreate() {
        super.onCreate();
        mScreenNavigator = new ScreenNavigator(this);
        button = new SimpleFloatButton(this, R.mipmap.ic_launcher);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
        windowManager.removeViewImmediate(button.getFloatingFaceBubble());
        windowManager.removeViewImmediate(theme.getView());
        timerExecutable.destroy();

        mScreenNavigator.toResultFromService(resultParams);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /*    private void startTimerAlpha(int timeAfter) {
        new CountDownTimer(timeAfter, 100) {

            public void onTick(long millisUntilFinished) {

                if (theme.getParams().alpha < 1f) {
                    Timber.i("" + millisUntilFinished);
                    theme.getBinding().tvExplanationTextFactoryResetDialog
                            .setText("seconds remaining: " + millisUntilFinished / 1000);
                    theme.getParams().alpha = theme.getParams().alpha + 0.01f;
                    windowManager.updateViewLayout(theme.getView(), theme.getParams());

                }

            }

            public void onFinish() {
                theme.getBinding().tvExplanationTextFactoryResetDialog
                        .setText("Ты не смог победить прокрастинацию, ты вытащим тебя");

            }
        }.start();
    }*/
      /*    private void startTimer(int time) {
      new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                theme.getBinding().tvExplanationTextFactoryResetDialog
                        .setText("seconds remaining: " + millisUntilFinished / 1000);

            }

            public void onFinish() {
                theme.getBinding().tvExplanationTextFactoryResetDialog
                        .setText("done!");

                // startTimerAlpha(timeAfter);
            }
        }.start();
    }*/

}