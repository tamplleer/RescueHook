package com.whynotpot.rescuehook.screens.main;

import static com.whynotpot.rescuehook.common.Constants.ONE_MINUTE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.slider.Slider;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.whynotpot.rescuehook.App;
import com.whynotpot.rescuehook.FastStartService;
import com.whynotpot.rescuehook.R;
import com.whynotpot.rescuehook.common.Constants;
import com.whynotpot.rescuehook.common.ScreenNavigator;
import com.whynotpot.rescuehook.common.SharedPreferenceSettings;
import com.whynotpot.rescuehook.common.ViewModelFactory;
import com.whynotpot.rescuehook.databinding.ActivityMainBinding;
import com.whynotpot.rescuehook.screens.overScreen.OverScreenFragment;
import com.whynotpot.rescuehook.service.BootReceiver;
import com.whynotpot.rescuehook.service.OverScreenService;
import com.whynotpot.rescuehook.service.Restarter;
import com.whynotpot.rescuehook.service.YourService;

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
    public static final String NEW_RESTART_SERVICE = "com.whynotpot.rescuehook.action.NEW_RESTART_SERVICE";
    private SharedPreferenceSettings preferenceSettings;
    private FragmentMainAdapter adapter;
    private String themeId;
    //Dagger Injection
    @Inject
    Context mContext;
    @Inject
    ViewModelFactory mViewModelFactory;

    //View binding
    private ActivityMainBinding mBinding;

    private JobScheduler scheduler;

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
    protected void onDestroy() {
        Timber.i("close");
      /*  Intent broadcastIntent = new Intent(NEW_RESTART_SERVICE);
        broadcastIntent.setPackage("com.whynotpot.rescuehook");
        this.sendBroadcast(broadcastIntent, null);*/
        Timber.i("close2");

        //  scheduler.cancel(123);
        //    Intent broadcastIntent = new Intent();
        //    broadcastIntent.setAction("restartservice");
        //    broadcastIntent.setPackage("com.whynotpot.servicetest");
        //   this.sendBroadcast(broadcastIntent);
        //  broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        //   broadcastIntent.setClass(this, Restarter.class);
        //   this.sendOrderedBroadcast(broadcastIntent, null)
        // ;
        adapter.clear();
        super.onDestroy();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {

        if (FastStartService.isMyServiceRunning) {
            Timber.i("Running");
            return true;
        }
        FastStartService.isMyServiceRunning = true;
        Timber.i("Not running");
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        preferenceSettings = new SharedPreferenceSettings(this);
        int time = preferenceSettings.getTime();

        if (!Settings.canDrawOverlays(this)) {
            AlertDialog.Builder ad;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            ad = new AlertDialog.Builder(this);
            ad.setTitle("Сообщение");// заголовок
            ad.setMessage("Чтобы использовать приложение нужноактивировать отображение поверх других окон"); // сообщение

            ad.setNegativeButton("Активировать", (dialog, arg1) -> startActivityForResult(intent, 0));
            ad.setPositiveButton("Закрыть", (dialog, arg1) -> {
                dialog.cancel();
                finish();
            });

            ad.setCancelable(true);

            ad.setOnCancelListener(dialog -> {
                dialog.cancel();
                finish();
            });
            ad.show();


        }

/*
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
*/

        //Dagger injection
        App.getComponent().inject(this);
        mMainViewModel = new ViewModelProvider(this, mViewModelFactory).get(MainViewModel.class);
        mScreenNavigator = new ScreenNavigator(this, getSupportFragmentManager());
        mMainViewModel.setCoins(preferenceSettings.getCoins());

        adapter = new FragmentMainAdapter(this, LayoutInflater.from(this), getSupportFragmentManager());
        mBinding.mainPager.setAdapter(adapter);
        mBinding.mainPager.setOffscreenPageLimit(3);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
            themeId = adapter.getThemeId((int) position);
        });
        mBinding.mainPager.setPageTransformer(compositePageTransformer);

        // mMainViewModel.dataSourceUpdate();
        mBinding.slider.setValue(time);
        mMainViewModel.setTimeTimer(time);
        mMainViewModel.setTimeView(time);
        mBinding.setMainViewModel(mMainViewModel);
        // ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //binding.tvTime.setTime;
        mBinding.slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NotNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NotNull Slider slider) {
                mMainViewModel.setTimeTimer((int) (slider.getValue()));
            }
        });
        mBinding.slider.addOnChangeListener((slider, value, fromUser) -> mMainViewModel.setTimeView((int) (slider.getValue())));

        mBinding.tvTime.setOnClickListener(view -> {
            openService();


        });
        mBinding.bStart.setOnClickListener(view -> {
            preferenceSettings.saveTime(mMainViewModel.getTimeTimerLiveData().getValue() / ONE_MINUTE);
            openService();
            //openTimePicker();
        });
        mBinding.swFastStart.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                if (!isMyServiceRunning(FastStartService.class)) {
                    Intent broadcastIntent = new Intent(NEW_RESTART_SERVICE);
                    broadcastIntent.setPackage("com.whynotpot.rescuehook");
                    broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendBroadcast(broadcastIntent);
                }
            } else {
                Intent broadcastIntent = new Intent("STOP");
                broadcastIntent.setPackage("com.whynotpot.rescuehook");
                sendBroadcast(broadcastIntent);
            }
        });

        //   Intent broadcastIntent = new Intent(NEW_RESTART_SERVICE);
        //broadcastIntent.setAction(NEW_RESTART_SERVICE);
        //   broadcastIntent.setPackage("com.whynotpot.rescuehook");
        //   broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        //broadcastIntent.setClass(this, BootReceiver.class);
        //     this.sendOrderedBroadcast(broadcastIntent, null);
        if (Build.BRAND.equals("xiaomi")) {
          /*  Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);*/

        }
        //   startService(new Intent(this, YourService.class));

/*                ComponentName componentName = new ComponentName(this, FastStartService.class);
                JobInfo jobInfo = new JobInfo.Builder(123, componentName)
                        .setPersisted(true)
                       .setPeriodic(1000*60*15)
                        .build();
                JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                int resultCode = scheduler.schedule(jobInfo);
                if (resultCode == JobScheduler.RESULT_SUCCESS){
                    Timber.i("Success");
                }
                else {
                    Timber.i("failure");
                }*/

/*            final Intent[] POWERMANAGER_INTENTS = {
                    new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
                    new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
                    new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
                    new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
                    new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
                    new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
                    new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
                    new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
                    new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
                    new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
                    new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
                    new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.battery.ui.BatteryActivity")),
                    new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
                    new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
                    new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity")),
                    new Intent().setComponent(new ComponentName("com.transsion.phonemanager", "com.itel.autobootmanager.activity.AutoBootMgrActivity"))
            };

            for (Intent intent : POWERMANAGER_INTENTS)
                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    // show dialog to ask user action
                    break;
                }
        });*/

        scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);


        // observeTimeTimer();


        //  LinearLayout linearLayout = (LinearLayout) findViewById(R.id.test_fragment);
        //mBinding.testLinear.setLayoutManager(new LinearLayoutManager(mContext));
        //setContentView(linearLayout);

        //work


/*        OverScreenFragment overScreenFragment = OverScreenFragment.getInstance();
        mBinding.fcvTest.setTag(R.id.test_fragment, overScreenFragment);
        mBinding.fcvTest.addView();*/



/*        YourService mYourService = new YourService();
        Intent mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }*/
    }

    private void openTimePicker() {
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
                        .putExtra(Constants.THEME_INTENT, themeId)
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

}