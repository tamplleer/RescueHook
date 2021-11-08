package com.whynotpot.rescuehook.floatButton;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;

import com.whynotpot.rescuehook.themes.CallBack;
import com.whynotpot.rescuehook.themes.Theme;

import timber.log.Timber;

public class FastStartButton {
    private ImageView floatingFaceBubble;
    private WindowManager.LayoutParams myParams;
    private WindowManager windowManager;
    private Theme theme;
    private final int INITIAL_X = 30;
    private final int INITIAL_Y = 100;

    public FastStartButton(Context context, int image, WindowManager windowManager, Theme theme) {
        this.windowManager = windowManager;
        this.theme = theme;
        floatingFaceBubble = new ImageView(context);
        floatingFaceBubble.setImageResource(image);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        myParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.RIGHT;
        myParams.x = INITIAL_X;
        myParams.y = INITIAL_Y;

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
                    //remove face bubble on long press
      /*              if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                        windowManager.removeView(floatingFaceBubble);

                        return false;
                    }*/
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Timber.i("down");
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            Timber.i("up");
                            myParams.x = initialX;
                            windowManager.updateViewLayout(v, myParams);
                            break;
                        case MotionEvent.ACTION_OUTSIDE:
                            Timber.i("outSide");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Timber.i(initialX + "   " + event.getRawX() + "   " + initialTouchX + "   " + (int) (event.getRawX() - initialTouchX));


                            //  if (Math.abs(initialY - (int) (event.getRawY() - initialTouchY)) >= 20) {
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            //   } else {
                            if (Math.abs((int) (event.getRawX() - initialTouchX)) > 300) {
                                myParams.x = 300;
                                windowManager.removeView(floatingFaceBubble);
                                windowManager.addView(theme.getView(), theme.getParams());

                            } else {
                                myParams.x = Math.abs((int) (event.getRawX() - initialTouchX));
                            }

                            //   }

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

    public ImageView getFloatingFaceBubble() {
        return floatingFaceBubble;
    }

    public WindowManager.LayoutParams getMyParams() {
        return myParams;
    }
}
