package com.whynotpot.rescuehook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.whynotpot.rescuehook.common.ViewModelFactory;
import com.whynotpot.rescuehook.databinding.ActivityMainBinding;
import com.whynotpot.rescuehook.databinding.FragmentOverSreenBinding;

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
        mBinding.testText.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OverScreenService.class);
            startService(intent);


        });

        //  LinearLayout linearLayout = (LinearLayout) findViewById(R.id.test_fragment);
        //mBinding.testLinear.setLayoutManager(new LinearLayoutManager(mContext));
        //setContentView(linearLayout);

        //work
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        OverScreenFragment overScreenFragment = OverScreenFragment.getInstance();
        ft.add(R.id.container, overScreenFragment);
        ft.commit();

/*        OverScreenFragment overScreenFragment = OverScreenFragment.getInstance();
        mBinding.fcvTest.setTag(R.id.test_fragment, overScreenFragment);
        mBinding.fcvTest.addView();*/


    }

    public FragmentManager getGSupportFragmentManager() {
        return getSupportFragmentManager();
    }

    public View getView() {
        return ActivityMainBinding.inflate(getLayoutInflater()).getRoot();
    }

    @SuppressLint("DefaultLocale")
    private void observeTestLiveData(@NotNull int number) {
        mBinding.testText.setText(String.format("number = %d", number));
        Timber.i("number = %s", number);

    }
}