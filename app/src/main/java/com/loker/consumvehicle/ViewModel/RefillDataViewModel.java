package com.loker.consumvehicle.ViewModel;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;
import com.loker.consumvehicle.repository.RefillDataRepository;

import java.util.List;

public class RefillDataViewModel extends AndroidViewModel {

    private MediatorLiveData<Boolean> success = new MediatorLiveData<>();

    private RefillDataRepository refillDataRepository;
    private LiveData<List<Refill>> carRefillList;
    private String carName;


    public RefillDataViewModel(@NonNull Application application, String carName) {
        super(application);
        refillDataRepository = new RefillDataRepository();
        carRefillList = refillDataRepository.getCarRefills(carName);
        this.carName = carName;
    }

    /*public void resetSuccess(){
        if(success.getValue()!=null && success.getValue())
            success.setValue(false);
        else
            success.setValue(true);
    }*/


    public LiveData<List<Refill>> getCarRefills(final String carName){

        return refillDataRepository.getCarRefills(carName);
    }

    public LiveData<Boolean> addRefill(Refill refill){

        return refillDataRepository.addRefill(refill);
    }

    public void deleteCarRefills(String carName){
        refillDataRepository.deleteCarRefills(carName);
    }

    public LiveData<Boolean> deleteRefill(Refill refill){
        return refillDataRepository.deleteRefill(refill);
    }
}
