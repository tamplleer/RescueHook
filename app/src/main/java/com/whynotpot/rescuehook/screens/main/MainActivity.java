package com.whynotpot.rescuehook.screens.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.whynotpot.rescuehook.App;
import com.whynotpot.rescuehook.common.Constants;
import com.whynotpot.rescuehook.common.ScreenNavigator;
import com.whynotpot.rescuehook.screens.overScreen.OverScreenFragment;
import com.whynotpot.rescuehook.service.OverScreenService;
import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.common.ViewModelFactory;
import com.whynotpot.rescuehook.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import javax.inject.Inject;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mMainViewModel;
    private ScreenNavigator mScreenNavigator;
    //Dagger Injection
    @Inject
    Context mContext;
    @Inject
    ViewModelFactory mViewModelFactory;

    //View binding
    private ActivityMainBinding mBinding;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //Dagger injection
        App.getComponent().inject(this);
        mMainViewModel = new ViewModelProvider(this, mViewModelFactory).get(MainViewModel.class);
        mScreenNavigator = new ScreenNavigator(this, getSupportFragmentManager());
        mMainViewModel.getTestLiveData().observe(this, this::observeTestLiveData);
        mMainViewModel.dataSourceUpdate();
        mBinding.testText.setOnClickListener(view -> {
            openService();


        });
        mBinding.button.setOnClickListener(view -> {
            openService();
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

    private void openService() {
        Intent intent1 = new Intent();
        PendingIntent pendingIntent = createPendingResult(1, intent1, 0);
        Intent intent;
        intent =
                new Intent(MainActivity.this, OverScreenService.class)
                        .putExtra(Constants.PENDING_INTENT, pendingIntent)
                        .putExtra(Constants.THEME_INTENT, "alpha")
                        .putExtra(Constants.TIME_BEFORE_INTENT, 6000000)
                        .putExtra(Constants.TIME_AFTER_INTENT, 500000);
        startService(intent);
    }

    public static Activity getInstance() {
        return getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show();
        mBinding.button.setText(String.format("%s", data.getStringExtra("cat")));

    }

    @SuppressLint("DefaultLocale")
    private void observeTestLiveData(@NotNull int number) {
        mBinding.testText.setText(String.format("number = %d", number));
        Timber.i("number = %s", number);

    }
}