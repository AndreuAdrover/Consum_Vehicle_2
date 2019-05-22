package com.loker.consumvehicle.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.loker.consumvehicle.model.Car;

//I need this class to be able to pass carName param to the RefillDataViewModel
public class RefillDataFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String carName;

    public RefillDataFactory(Application mApplication, String carName) {
        this.mApplication = mApplication;
        this.carName = carName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RefillDataViewModel(mApplication,carName);
    }
}

/*public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public MyViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MyViewModel(mApplication, mParam);
    }
}*/