package com.whynotpot.rescuehook;

import android.annotation.SuppressLint;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.whynotpot.rescuehook.databinding.FragmentOverSreenBinding;

import timber.log.Timber;

public class OverScreenService extends Service {
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;
    private ImageView floatingFaceBubble2;
    private LinearLayout floatLayout;
    private OverScreenFragment myFragment;

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate() {
        super.onCreate();
        floatingFaceBubble = new ImageView(this);
        floatingFaceBubble2 = new ImageView(this);
        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.drawable.ic_launcher_foreground);
        floatingFaceBubble2.setImageResource(R.drawable.ic_launcher_background);
        floatLayout = new LinearLayout(this);

         myFragment = new OverScreenFragment();

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
        myParams.gravity = Gravity.TOP | Gravity.CENTER;
        myParams.x = 0;
        myParams.y = 100;
        final LayoutParams myParams2 = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams2.gravity = Gravity.TOP | Gravity.CENTER;
        myParams2.x = 0;
        myParams2.y = 100;

        LinearLayoutCompat linearLayout = new LinearLayoutCompat(this);
        linearLayout.setBackgroundResource(R.drawable.ic_launcher_background);
        linearLayout.setId(R.id.layout1);
        //linearLayout.addView(new OverScreenFragment());

       /* FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();

        ft.add(R.id.layout1, overScreenFragment);
        ft.commit();*/
        OverScreenFragment overScreenFragment = OverScreenFragment.getInstance();
        FragmentContainerView fragmentContainerView = new FragmentContainerView(this);
        fragmentContainerView.setTag(R.id.test_fragment, overScreenFragment);


        LayoutInflater inflater = LayoutInflater.from(this); // some context
        View row = inflater.inflate(R.layout.activity_main, null);


        // add a floatingfacebubble icon in window
        windowManager.addView(row, myParams2);
        windowManager.addView(floatingFaceBubble, myParams);



        try {

            floatingFaceBubble.setOnClickListener(view -> {
                Timber.i("click");
            });
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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}