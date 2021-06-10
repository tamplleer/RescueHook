package com.whynotpot.rescuehook.dagger;

import com.whynotpot.rescuehook.MainActivity;
import com.whynotpot.rescuehook.OverScreenFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(OverScreenFragment overScreenFragment);
}
