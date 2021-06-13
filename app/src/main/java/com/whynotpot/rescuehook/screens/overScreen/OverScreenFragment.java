package com.whynotpot.rescuehook.screens.overScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.whynotpot.rescuehook.App;
import com.whynotpot.rescuehook.common.ViewModelFactory;
import com.whynotpot.rescuehook.databinding.FragmentOverSreenBinding;

import javax.inject.Inject;

public class OverScreenFragment extends Fragment {
    private OverScreenViewModel overScreenViewModel;
    private FragmentActivity mActivity;
    private FragmentOverSreenBinding mBinding;
    //Dagger Injection
    @Inject
    ViewModelFactory mViewModelFactory;

    public static OverScreenFragment getInstance() {
        return new OverScreenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        // mScreenNavigator = new ScreenNavigator(mActivity, mActivity.getSupportFragmentManager());
        ((App) mActivity.getApplication()).getComponent().inject(this);
        overScreenViewModel = new ViewModelProvider(mActivity, mViewModelFactory).get(OverScreenViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentOverSreenBinding.inflate(inflater, container, false);
        // initToolbar();
        //   mBinding.btnFactoryReset.setOnClickListener(v -> mScreenNavigator.replaceFragment(new FactoryResetPerformingFragment(), R.id.fcv_factory_reset));
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }
}
