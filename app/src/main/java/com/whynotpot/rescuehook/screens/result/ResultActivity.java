package com.whynotpot.rescuehook.screens.result;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.whynotpot.rescuehook.App;
import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.common.ScreenNavigator;
import com.whynotpot.rescuehook.common.SharedPreferenceSettings;
import com.whynotpot.rescuehook.common.ViewModelFactory;
import com.whynotpot.rescuehook.databinding.ActivityMainBinding;
import com.whynotpot.rescuehook.databinding.ActivityResultBinding;
import com.whynotpot.rescuehook.screens.main.MainActivity;
import com.whynotpot.rescuehook.screens.main.MainViewModel;

import javax.inject.Inject;

public class ResultActivity extends AppCompatActivity {
    private ResultViewModel mResultViewModel;
    private ActivityResultBinding mBinding;
    private ScreenNavigator mScreenNavigator;
    private SharedPreferenceSettings preferenceSettings;
    @Inject
    Context mContext;
    @Inject
    ViewModelFactory mViewModelFactory;

    public static void startCloseService(Context context, ResultParams resultParams) {
        Intent dialogIntent = new Intent(context, ResultActivity.class);
        if (resultParams != null) {
            dialogIntent.putExtra("params", resultParams);
        }
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialogIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        preferenceSettings = new SharedPreferenceSettings(this);


        //Dagger injection
        App.getComponent().inject(this);
        mResultViewModel = new ViewModelProvider(this, mViewModelFactory).get(ResultViewModel.class);
        mScreenNavigator = new ScreenNavigator(this, getSupportFragmentManager());
        ResultParams resultParams = getIntent().getParcelableExtra("params");
        mBinding.setResultViewModel(mResultViewModel);
        if (resultParams != null) {
            mResultViewModel.setRemainingTime(resultParams.getRemainingTime());
            preferenceSettings.saveCoins(resultParams.getRemainingTime());
        }
        mBinding.bBack.setOnClickListener(view -> {
            mScreenNavigator.toMain();
        });
    }
}