package com.whynotpot.rescuehook;

import android.app.Application;

import androidx.annotation.NonNull;

import com.whynotpot.rescuehook.dagger.AppComponent;
import com.whynotpot.rescuehook.dagger.AppModule;
import com.whynotpot.rescuehook.dagger.DaggerAppComponent;
import com.whynotpot.rescuehook.timber.TimberReleaseTree;

import timber.log.Timber;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        //dagger
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        //Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                //Add line number to tag
                @Override
                protected String createStackElementTag(@NonNull StackTraceElement element) {
                    //Fix to avoid Timber bug with length messages in error Log level,
                    // if tag is to long we take out method name
                    String tag = '(' + element.getFileName() + ':' + element.getLineNumber() + ") - " +
                            element.getMethodName() + "()";
                    int maximumNumberOfChars = 85;
                    if (tag.length() > maximumNumberOfChars) {
                        tag = '(' + element.getFileName() + ':' + element.getLineNumber() + ") - " + "()";
                    }
                    return tag;
                }
            });

        } else {
            //if ever is used Crashlytics shuold be initialized here
            Timber.plant(new TimberReleaseTree());
        }
    }

    public static AppComponent getComponent() {
        return component;
    }

}
