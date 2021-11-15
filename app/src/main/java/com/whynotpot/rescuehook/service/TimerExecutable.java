package com.whynotpot.rescuehook.service;

import android.view.WindowManager;

import com.whynotpot.rescuehook.themes.Theme;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TimerExecutable {
    private Theme theme;
    final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private WindowManager windowManager;
    private int remainingTime = 0;

    public TimerExecutable(Theme theme, int time, WindowManager windowManager) {
        this.theme = theme;
        this.windowManager = windowManager;
        startAlpha(time);

    }

    private void startAlpha(int timeAfter) {
        mCompositeDisposable.add(dataSource(timeAfter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(num -> {
                    if (theme.getParams().alpha < 1f) {
                        theme.getParams().alpha = Math.min(theme.getParams().alpha + 0.005f, 1f);
                 /*       if (theme.getParams().flags == 264) {
                            theme.setFlagNotTouch();
                        } else {
                            theme.setFlagTouch();//todo тест сложного скрола
                        }*/
                        Timber.i(theme.getParams().flags + "");
                        windowManager.updateViewLayout(theme.getView(), theme.getParams());
                    } else {
                        /* Toast.makeText(this, "EEEEEEEE",
                                Toast.LENGTH_SHORT).show();*/
                    }

                    Timber.i("next alpha " + theme.getParams().alpha);
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
                    setRemainingTime(i);
                    subscriber.onNext(i);
                }
                setRemainingTime(0);
                subscriber.onComplete();
            } catch (InterruptedException ex) {
                if (!subscriber.isDisposed()) {
                    subscriber.onError(ex);
                }
            }

        });
    }

    public void destroy() {
        mCompositeDisposable.dispose();
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
