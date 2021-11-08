package com.whynotpot.rescuehook.themes;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.whynotpot.rescuehook.common.Constants;
import com.whynotpot.rescuehook.databinding.FragmentFastStartBinding;
import com.whynotpot.rescuehook.databinding.FragmentOverSreenBinding;
import com.whynotpot.rescuehook.screens.main.MainActivity;
import com.whynotpot.rescuehook.service.OverScreenService;

import timber.log.Timber;

public class ThemeFastStart extends Fragment implements Theme {

    private WindowManager.LayoutParams params;
    private FragmentFastStartBinding mBinding;
    private CallBack callBack;

    public ThemeFastStart(CallBack callBack) {
        this.callBack = callBack;

        //todo вынести это выше в абстрактный класс и почистить
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.x = 0;
        params.y = 100;
        params.alpha = 1;

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

        mBinding = FragmentFastStartBinding.inflate(inflater);
        mBinding.bFastStartService.setOnClickListener(view -> {
            Timber.i("start");
            callBack.callbackCall();
        });
    }

    public View getView() {
        return mBinding.getRoot();
    }

    public FragmentFastStartBinding getBinding() {
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
