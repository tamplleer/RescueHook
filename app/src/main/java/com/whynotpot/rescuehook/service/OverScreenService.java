package com.whynotpot.rescuehook.service;

import android.annotation.SuppressLint;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.whynotpot.rescuehook.App;
import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.common.Constants;
import com.whynotpot.rescuehook.common.ScreenNavigator;
import com.whynotpot.rescuehook.common.ViewModelFactory;
import com.whynotpot.rescuehook.databinding.ActivityMainBinding;
import com.whynotpot.rescuehook.databinding.ActivityOverScreenBinding;
import com.whynotpot.rescuehook.databinding.FragmentOverSreenBinding;
import com.whynotpot.rescuehook.screens.main.MainActivity;
import com.whynotpot.rescuehook.screens.overScreen.OverScreenFragment;
import com.whynotpot.rescuehook.screens.overScreen.OverScreenViewModel;
import com.whynotpot.rescuehook.themes.ThemeSimpleAlpha;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class OverScreenService extends Service {
    private WindowManager windowManager;
    private ThemeSimpleAlpha theme;

    private ImageView floatingFaceBubble;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pendingIntent = intent.getParcelableExtra(Constants.PENDING_INTENT);
        int timeBefore = intent.getIntExtra(Constants.TIME_BEFORE_INTENT,180000);
        int timeAfter = intent.getIntExtra(Constants.TIME_BEFORE_INTENT,15000);
        String theme1 = intent.getStringExtra(Constants.THEME_INTENT);
       theme.onStartCommand(pendingIntent);
       startTimer(timeBefore,timeAfter);
        Intent intents = new Intent().putExtra("cat","Cat");
        floatingFaceBubble.setOnClickListener(view -> {


            try {
                pendingIntent.send(this, Constants.SERVICE_OVER_SCREEN_REQUEST_CODE,intents);
              //  startAlpha();
                stopSelfResult(startId);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        });

        return super.onStartCommand(intent, flags, startId);

    }
    private void startTimer(int timeBefore, int timeAfter){
        new CountDownTimer(timeBefore, 1000) {

            public void onTick(long millisUntilFinished) {
                theme.getBinding().tvExplanationTextFactoryResetDialog
                        .setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                theme.getBinding().tvExplanationTextFactoryResetDialog
                        .setText("done!");
               // startAlpha(timeAfter);
                startTimerAlpha(timeAfter);
            }
        }.start();
    }
    private void startTimerAlpha(int timeAfter){
        new CountDownTimer(timeAfter, 100) {

            public void onTick(long millisUntilFinished) {

                if ( theme.getMyParams2().alpha < 1f){
                    Timber.i(""+millisUntilFinished);
                    theme.getBinding().tvExplanationTextFactoryResetDialog
                            .setText("seconds remaining: " + millisUntilFinished / 1000);
                    theme.getMyParams2().alpha =  theme.getMyParams2().alpha+0.01f ;
                    windowManager.updateViewLayout(theme.getView(),theme.getMyParams2());
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
        floatingFaceBubble = new ImageView(this);
        floatingFaceBubble.setImageResource(R.drawable.ic_launcher_foreground);


       //floatLayout.findViewById(R.id.test_linear);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //here is all the science of params

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final LayoutParams myParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.RIGHT;
        myParams.x = 0;
        myParams.y = 100;




        theme = new ThemeSimpleAlpha();
        LayoutInflater inflater = LayoutInflater.from(this);
        theme.onCreate(inflater);




        windowManager.addView(theme.getView(), theme.getMyParams2());
        windowManager.addView(floatingFaceBubble, myParams);



        try {
            //for moving the picture on touch and slide
            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Timber.i("touch");
                    //remove face bubble on long press
                    if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                        windowManager.removeView(floatingFaceBubble);
                        stopSelf();
                        return false;
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAlpha(int timeAfter){
        Disposable disc = dataSource(timeAfter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(num -> {
                    if ( theme.getMyParams2().alpha < 1f){
                        theme.getMyParams2().alpha =  theme.getMyParams2().alpha+0.005f ;
                        windowManager.updateViewLayout(theme.getView(),theme.getMyParams2());
                    }
                    else {
                        Toast.makeText(this, "EEEEEEEE",
                                Toast.LENGTH_SHORT).show();
                    }

                    Timber.i("next alpha " + theme.getMyParams2().alpha);
                });
    }

    private Observable<Integer> dataSource(int timeAfter) {
        return Observable.create((subscriber) ->
                {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(300);
                        subscriber.onNext(i);
                    }
                    subscriber.onComplete();
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
        windowManager.removeViewImmediate(floatingFaceBubble);
        windowManager.removeViewImmediate(theme.getView());

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}