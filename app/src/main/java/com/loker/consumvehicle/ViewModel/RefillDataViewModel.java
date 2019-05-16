package com.loker.consumvehicle.ViewModel;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;
import com.loker.consumvehicle.repository.RefillDataRepository;

import java.util.List;

public class RefillDataViewModel extends AndroidViewModel {

    private LiveData<Boolean> addRefillSuccess = new MutableLiveData<>();

    private RefillDataRepository refillDataRepository;
    private LiveData<List<Refill>> carRefillList;


    public RefillDataViewModel(@NonNull Application application) {
        super(application);

        refillDataRepository = new RefillDataRepository();
    }


    public LiveData<List<Refill>> getCarRefills(String carName){

        return refillDataRepository.getCarRefills(carName);
    }

    public LiveData<Boolean> addRefill(Refill refill){

        return refillDataRepository.addRefill(refill);
    }
}
