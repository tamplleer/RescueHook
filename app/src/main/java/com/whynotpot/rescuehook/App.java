package com.whynotpot.rescuehook;

import android.app.Application;

import com.whynotpot.rescuehook.dagger.AppComponent;
import com.whynotpot.rescuehook.dagger.AppModule;
import com.whynotpot.rescuehook.dagger.DaggerAppComponent;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }

}
