package com.whynotpot.rescuehook.service;

import android.annotation.SuppressLint;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import com.whynotpot.rescuehook.common.Constants;
import com.whynotpot.rescuehook.floatButton.SimpleFloatButton;
import com.whynotpot.rescuehook.themes.ThemeSimpleAlpha;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class OverScreenService extends Service {
    private WindowManager windowManager;
    private ThemeSimpleAlpha theme;
    private SimpleFloatButton button;
    final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pendingIntent = intent.getParcelableExtra(Constants.PENDING_INTENT);
        int time = intent.getIntExtra(Constants.TIME, 5000);
        String theme1 = intent.getStringExtra(Constants.THEME_INTENT);
        theme.onStartCommand(pendingIntent);
        startTimer(time);
        Intent intents = new Intent().putExtra("cat", "Cat");
        button.getFloatingFaceBubble().setOnClickListener(view -> {
            try {
                pendingIntent.send(this, Constants.SERVICE_OVER_SCREEN_REQUEST_CODE, intents);
                stopSelfResult(startId);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        });

        return super.onStartCommand(intent, flags, startId);

    }

    private void startTimer(int time) {
        startAlpha(time);
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
    }

    private void startTimerAlpha(int timeAfter) {
        new CountDownTimer(timeAfter, 100) {

            public void onTick(long millisUntilFinished) {

                if (theme.getMyParams2().alpha < 1f) {
                    Timber.i("" + millisUntilFinished);
                    theme.getBinding().tvExplanationTextFactoryResetDialog
                            .setText("seconds remaining: " + millisUntilFinished / 1000);
                    theme.getMyParams2().alpha = theme.getMyParams2().alpha + 0.01f;
                    windowManager.updateViewLayout(theme.getView(), theme.getMyParams2());
                }

            }

            public void onFinish() {
                theme.getBinding().tvExplanationTextFactoryResetDialog
                        .setText("Ты не смог победить прокрастинацию, ты вытащим тебя");

            }
        }.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate() {
        super.onCreate();
        button = new SimpleFloatButton(this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        theme = new ThemeSimpleAlpha();
        LayoutInflater inflater = LayoutInflater.from(this);
        theme.onCreate(inflater);


        windowManager.addView(theme.getView(), theme.getMyParams2());
        windowManager.addView(button.getFloatingFaceBubble(), button.getMyParams());


    }

    private void startAlpha(int timeAfter) {
        mCompositeDisposable.add(dataSource(timeAfter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(num -> {
                    if (theme.getMyParams2().alpha < 1f) {
                        theme.getMyParams2().alpha = Math.min(theme.getMyParams2().alpha + 0.005f, 1f);
                        windowManager.updateViewLayout(theme.getView(), theme.getMyParams2());
                    } else {
                        /* Toast.makeText(this, "EEEEEEEE",
                                Toast.LENGTH_SHORT).show();*/
                    }

                    Timber.i("next alpha " + theme.getMyParams2().alpha);
                }));
    }

    private Observable<Integer> dataSource(int time) {
        //todo проверка если время 0 или null
        int sleepTime = time / 200;
        int maxTime = time / sleepTime + 5;
        return Observable.create((subscriber) ->
        {
            try {


            for (int i = 0; i < maxTime; i++) {
                    Thread.sleep(sleepTime);
                    subscriber.onNext(i);

            }
            subscriber.onComplete();
              }
            catch (InterruptedException ex) {
            if (!subscriber.isDisposed()) {
                subscriber.onError(ex);
            }
        }

        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
        windowManager.removeViewImmediate(button.getFloatingFaceBubble());
        windowManager.removeViewImmediate(theme.getView());
        mCompositeDisposable.dispose();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}