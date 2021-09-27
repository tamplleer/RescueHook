package com.whynotpot.rescuehook.screens.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.whynotpot.rescuehook.App;
import com.whynotpot.rescuehook.common.Constants;
import com.whynotpot.rescuehook.common.ScreenNavigator;
import com.whynotpot.rescuehook.screens.overScreen.OverScreenFragment;
import com.whynotpot.rescuehook.service.OverScreenService;
import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.common.ViewModelFactory;
import com.whynotpot.rescuehook.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void startCloseService(Context context) {
        Intent dialogIntent = new Intent(context, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialogIntent);
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
       // mMainViewModel.dataSourceUpdate();
        mBinding.slider.setValue(3f);
        mMainViewModel.setTimeTimer(3*60000);
       mBinding.slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
           @Override
           public void onStartTrackingTouch( @NotNull Slider slider) {
           }

           @Override
           public void onStopTrackingTouch( @NotNull Slider slider) {
               mMainViewModel.setTimeTimer((int) (slider.getValue()*60000));
           }
       });
       mBinding.slider.addOnChangeListener(new Slider.OnChangeListener() {
           @Override
           public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
              // mBinding.testText.setText(" "+value);
           }
       });

        mBinding.testText.setOnClickListener(view -> {
            openService();


        });
        mBinding.bStart.setOnClickListener(view -> {
            openService();
            //openTimePicker();
        });


       // observeTimeTimer();


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
   private void observeTimeTimer(){
       mBinding.edTimer.setOnEditorActionListener((v, actionId, event) -> {
           if (actionId == EditorInfo.IME_ACTION_DONE) {
               if (mBinding.edTimer.getText().toString().isEmpty()) {
                   mBinding.bStart.setEnabled(false);
               } else {
                   mBinding.bStart.setEnabled(true);
                   DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
                   try {
                       Timber.i("%s","yees");
                       Date date = formatter.parse(mBinding.edTimer.getText().toString());
                       mMainViewModel.setTimeTimer((int)date.getTime());
                   } catch (ParseException e) {
                       Timber.i("%s",e.getMessage());
                       e.printStackTrace();
                   }

                   Timber.i("%s", mMainViewModel.getTimeTimerLiveData().getValue());
               }
           }
           return false;
       });
    }
    private void openTimePicker(){
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(10)
                        .build();
        picker.show(getSupportFragmentManager(), "tag");
    }
    private void openService() {
        Intent intent1 = new Intent();
        PendingIntent pendingIntent = createPendingResult(1, intent1, 0);
        Intent intent;
        intent =
                new Intent(MainActivity.this, OverScreenService.class)
                        .putExtra(Constants.PENDING_INTENT, pendingIntent)
                        .putExtra(Constants.THEME_INTENT, "alpha")
                        .putExtra(Constants.TIME, mMainViewModel.getTimeTimerLiveData().getValue());
        startService(intent);
        finish();
    }

    public static Activity getInstance() {
        return getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show();
        mBinding.bStart.setText(String.format("%s", data.getStringExtra("cat")));

    }

    @SuppressLint("DefaultLocale")
    private void observeTestLiveData(@NotNull int number) {
      //  mBinding.testText.setText(String.format("number = %d", number));
        Timber.i("number = %s", number);

    }
}