package com.whynotpot.rescuehook.dagger;

import com.whynotpot.rescuehook.screens.main.MainActivity;
import com.whynotpot.rescuehook.screens.overScreen.OverScreenFragment;
import com.whynotpot.rescuehook.screens.result.ResultActivity;
import com.whynotpot.rescuehook.service.OverScreenService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(OverScreenFragment overScreenFragment);

    void inject(ResultActivity resultActivity);
}
