package com.whynotpot.rescuehook.screens.result;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultParams implements Parcelable {
    private int remainingTime;

    public ResultParams(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    protected ResultParams(Parcel in) {
        remainingTime = in.readInt();
    }

    public static final Creator<ResultParams> CREATOR = new Creator<ResultParams>() {
        @Override
        public ResultParams createFromParcel(Parcel in) {
            return new ResultParams(in);
        }

        @Override
        public ResultParams[] newArray(int size) {
            return new ResultParams[size];
        }
    };

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(remainingTime);
    }
}
