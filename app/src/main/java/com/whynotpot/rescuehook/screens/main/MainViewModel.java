package com.whynotpot.rescuehook.screens.main;

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
    private final MutableLiveData<Integer> _testData = new MutableLiveData<Integer>();
    private final int ONE_MINUTE = 60000;

    public LiveData<Integer> getTestLiveData() {
        return _testData;
    }

    private final MutableLiveData<Integer> _timeTimer = new MutableLiveData<Integer>();
    private final ObservableField<String> timeTimerString = new ObservableField<>();

    public ObservableField<String> getTimeTimerString() {
        return timeTimerString;
    }

    public LiveData<Integer> getTimeTimerLiveData() {
        return _timeTimer;
    }

    public void setTimeTimer(int time) {
        _timeTimer.postValue(time * ONE_MINUTE);
    }

    public void setTimeView(int time) {
        timeTimerString.set(time + " мин");
    }

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    MainViewModel() {
    }

    public Observable<Integer> testDataUpdate() {
        return Observable.create((subscriber) ->
                {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(1000);
                        subscriber.onNext(i);
                    }
                    subscriber.onComplete();
                }
        );
    }

    public void dataSourceUpdate() {
        mCompositeDisposable.add(testDataUpdate()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(num -> {
                    _testData.setValue(num);
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();

    }
}
