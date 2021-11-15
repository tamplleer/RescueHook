package com.whynotpot.rescuehook.screens.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.databinding.FragmentCardThemeBinding;
import com.whynotpot.rescuehook.screens.overScreen.OverScreenFragment;
import com.whynotpot.rescuehook.screens.overScreen.OverScreenViewModel;

public class FragmentCardTheme extends Fragment {
    private FragmentActivity mActivity;
    private FragmentCardThemeBinding mBinding;
    private Fragment fragment;


    public FragmentCardTheme(Fragment fragment) {
        this.fragment = fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCardThemeBinding.inflate(inflater, container, false);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(mBinding.cardContainer.getId(), fragment)
                .setReorderingAllowed(true)
                .addToBackStack("")
                .commit();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }
}
