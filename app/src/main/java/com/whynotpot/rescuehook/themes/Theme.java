package com.whynotpot.rescuehook.themes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public interface Theme {
    View getBindingView();

    void onCreate(LayoutInflater inflater);

    View getView();

    WindowManager.LayoutParams getParams();

    void setFlagNotTouch();//todo delete and сделать общий

    void setFlagTouch();

}
