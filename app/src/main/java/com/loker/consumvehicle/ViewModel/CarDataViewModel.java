package com.loker.consumvehicle.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.User;
import com.loker.consumvehicle.repository.CarDataRepository;

import java.util.List;

public class CarDataViewModel extends AndroidViewModel {

    private CarDataRepository carDataRepository;
    private LiveData<List<Car>> userCarList;
    private MutableLiveData<Boolean> addCarSucces = new MutableLiveData<>();
    private MutableLiveData<Boolean> removeCarSucces = new MutableLiveData<>();

    public CarDataViewModel(@NonNull Application application) {
        super(application);
        carDataRepository = new CarDataRepository();
        userCarList = carDataRepository.getUserCarList();

    }

    public LiveData<Boolean> addCar(Car newCar){

        return carDataRepository.addCar(newCar);

    }

    public void resetAddCarSuccess(){
        if(addCarSucces.getValue()!=null && addCarSucces.getValue())
            addCarSucces.setValue(false);
        else
            addCarSucces.setValue(true);
    }
    public void resetRemoveSuccess(){
        if(removeCarSucces.getValue()!=null && removeCarSucces.getValue())
            removeCarSucces.setValue(false);
        else
            removeCarSucces.setValue(true);

    }
    /*public LiveData<List<Car>> getUserCarList(){

        return carDataRepository.getUserCarList();
    }*/

    public LiveData<List<Car>> getUserCarList(){

        final MediatorLiveData <List<Car>> mediatorCarList = new MediatorLiveData<>();

        mediatorCarList.addSource(addCarSucces, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.d("mediatorCarList","addCar");
                userCarList=carDataRepository.getUserCarList();
                mediatorCarList.setValue(userCarList.getValue());
            }
        });
        mediatorCarList.addSource(removeCarSucces, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.d("mediatorCarList","removeCar");
                userCarList = carDataRepository.getUserCarList();
                mediatorCarList.setValue(userCarList.getValue());
            }
        });
        mediatorCarList.addSource(userCarList, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {

                if(mediatorCarList.getValue()==null) {
                    mediatorCarList.setValue(carDataRepository.getUserCarList().getValue());
                    Log.d("mediatorCarList","CarListChanged_null");
                }
                else {
                    mediatorCarList.setValue(userCarList.getValue());
                    Log.d("mediatorCarList","CarListChanged"+mediatorCarList.getValue());
                }
            }
        });


        return mediatorCarList;
    }

    public LiveData<Boolean> deleteCar(Car carToDelte) {
        return carDataRepository.deleteCar(carToDelte);
    }
}
