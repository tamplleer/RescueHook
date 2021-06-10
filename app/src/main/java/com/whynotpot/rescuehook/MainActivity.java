package com.whynotpot.rescuehook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.whynotpot.rescuehook.common.ViewModelFactory;
import com.whynotpot.rescuehook.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mMainViewModel;
    //Dagger Injection
    @Inject
    Context mContext;
    @Inject
    ViewModelFactory mViewModelFactory;

    //View binding
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //Dagger injection
        App.getComponent().inject(this);
        mMainViewModel = new ViewModelProvider(this, mViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getTestLiveData().observe(this, this::observeTestLiveData);
        mMainViewModel.dataSourceUpdate();
    }

    @SuppressLint("DefaultLocale")
    private void observeTestLiveData(@NotNull int number) {
        mBinding.testText.setText(String.format("number = %d", number));
        Timber.i("number = %s", number);

    }
}