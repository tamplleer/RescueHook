package com.whynotpot.rescuehook.screens.main;

import static com.whynotpot.rescuehook.common.Constants.ONE_MINUTE;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private final ObservableField<String> timeTimerString = new ObservableField<>();

    public ObservableField<String> getTimeTimerString() {
        return timeTimerString;
    }

    public void setTimeView(int time) {
        timeTimerString.set(time + " мин");
    }

    private final MutableLiveData<Integer> _timeTimer = new MutableLiveData<Integer>();

    public LiveData<Integer> getTimeTimerLiveData() {
        return _timeTimer;
    }

    public void setTimeTimer(int time) {
        _timeTimer.postValue(time * ONE_MINUTE);
    }

    private final ObservableField<String> _coins = new ObservableField<>();

    public ObservableField<String> getCoins() {
        return _coins;
    }

    public void setCoins(int coins) {
        _coins.set(String.format("%d монет", coins));
    }

    @Inject
    MainViewModel() {
    }
}
