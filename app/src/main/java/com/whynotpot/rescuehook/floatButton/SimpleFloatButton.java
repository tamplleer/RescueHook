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

import com.whynotpot.rescuehook.R;

import timber.log.Timber;

public class SimpleFloatButton {
    private ImageView floatingFaceBubble;
    private  WindowManager.LayoutParams myParams;

    public SimpleFloatButton(Context context) {
        floatingFaceBubble = new ImageView(context);
        floatingFaceBubble.setImageResource(R.mipmap.ic_launcher);

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
        myParams.gravity = Gravity.TOP | Gravity.RIGHT;
        myParams.x = 0;
        myParams.y = 100;

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
          /*          if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                        windowManager.removeView(floatingFaceBubble);
                        stopSelf();
                        return false;
                    }*/
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
                            //windowManager.updateViewLayout(v, myParams);
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
