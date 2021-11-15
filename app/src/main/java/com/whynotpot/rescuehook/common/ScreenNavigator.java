package com.whynotpot.rescuehook.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.screens.main.MainActivity;
import com.whynotpot.rescuehook.screens.result.ResultActivity;
import com.whynotpot.rescuehook.screens.result.ResultParams;


public class ScreenNavigator {

    private final Activity mActivity;
    private final FragmentManager mFragmentManager;
    private final Context context;

    public ScreenNavigator(Activity activity, FragmentManager fragmentManager) {
        mActivity = activity;
        mFragmentManager = fragmentManager;
        context = null;
    }

    public ScreenNavigator(Context context) {
        this.context = context;
        mActivity = null;
        mFragmentManager = null;
    }

    public void finishFactoryResetForResult(int resultCode) {
        Intent returnIntent = new Intent();
        mActivity.setResult(resultCode, returnIntent);
        mActivity.finish();
    }

    public void finishNodeCommandForResult() {
        Intent returnIntent = new Intent();
        mActivity.setResult(Constants.CLOSE_ACTIVITY, returnIntent);
        mActivity.finish();
    }

    public void toMain() {
        MainActivity.start(mActivity);
        mActivity.finish();
    }

    public void toMainFromService() {
        MainActivity.startCloseService(context);
    }
    public void toResultFromService(ResultParams resultParams) {
        ResultActivity.startCloseService(context,resultParams);
    }


    /**
     * Replace fragment with slide animation
     *
     * @param fragment
     */
    public void replaceFragment(Fragment fragment, int idFragment) {
        mFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(idFragment, fragment)
                .commit();

    }
}
