package com.whynotpot.rescuehook.common;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.whynotpot.rescuehook.R;


public class ScreenNavigator {

    private final Activity mActivity;
    private final FragmentManager mFragmentManager;

    public ScreenNavigator(Activity activity, FragmentManager fragmentManager) {
        mActivity = activity;
        mFragmentManager = fragmentManager;
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
