package com.whynotpot.rescuehook.screens.result;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ResultViewModel extends ViewModel {
    private final ObservableField<String> remainingTime = new ObservableField<>();

    public ObservableField<String> getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int time) {
        remainingTime.set(String.format("Ты набрал %s coins!", time));
    }

    @Inject
    ResultViewModel() {
    }
}
