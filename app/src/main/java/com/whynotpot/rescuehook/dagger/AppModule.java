package com.whynotpot.rescuehook.dagger;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.whynotpot.rescuehook.MainViewModel;
import com.whynotpot.rescuehook.OverScreenViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Singleton;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class AppModule {

    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return mApplication;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    ViewModel getMainViewModel(MainViewModel mainViewModel) {
        return mainViewModel;
    }

    @Provides
    @IntoMap
    @ViewModelKey(OverScreenViewModel.class)
    ViewModel getOverScreenViewModel(OverScreenViewModel overScreenViewModel) {
        return overScreenViewModel;
    }
}
