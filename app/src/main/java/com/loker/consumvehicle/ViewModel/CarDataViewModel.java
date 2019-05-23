package com.loker.consumvehicle.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
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

    public CarDataViewModel(@NonNull Application application) {
        super(application);
        carDataRepository = new CarDataRepository();
        userCarList = carDataRepository.getUserCarList();

    }

    public LiveData<Boolean> addCar(Car newCar){

        return carDataRepository.addCar(newCar);

    }

    public LiveData<List<Car>> getUserCarList(){

        return userCarList;
    }

    public LiveData<Boolean> deleteCar(Car carToDelte) {
        return carDataRepository.deleteCar(carToDelte);
    }

    public void updateCar(Car car){
        carDataRepository.updateCar(car);
    }

    public LiveData<Bitmap> getImageCar(Car car){
        return carDataRepository.getCarImage(car);
    }
}
