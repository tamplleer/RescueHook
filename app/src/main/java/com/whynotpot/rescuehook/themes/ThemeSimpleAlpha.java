package com.whynotpot.rescuehook.themes;

import android.app.PendingIntent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.whynotpot.rescuehook.databinding.FragmentOverSreenBinding;

public class ThemeSimpleAlpha extends Fragment implements Theme {

    private WindowManager.LayoutParams params;
    private FragmentOverSreenBinding mBinding;

    public ThemeSimpleAlpha() {

        //todo вынести это выше в абстрактный класс и почистить
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.x = 0;
        params.y = 100;
        params.alpha = 0;

    }

    public void setFlagTouch() {
        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    public void setFlagNotTouch() {
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    public void onStartCommand(PendingIntent pendingIntent) {
    }

    public void onCreate(LayoutInflater inflater) {

        mBinding = FragmentOverSreenBinding.inflate(inflater);
    }

    public View getView() {
        return mBinding.getRoot();
    }

    public FragmentOverSreenBinding getBinding() {
        return mBinding;
    }

    public WindowManager.LayoutParams getParams() {
        return params;
    }

    @Override
    public View getBindingView() {
        return null;
    }
}
