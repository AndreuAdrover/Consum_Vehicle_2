package com.loker.consumvehicle.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.loker.consumvehicle.model.User;
import com.loker.consumvehicle.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private LiveData<User> currentUser;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository =  new LoginRepository(application);
        currentUser = repository.getUser();
    }

    public LiveData<User> getCurrenUser(){
        return currentUser;
    }

    public void signOut(){
        repository.signOut();
    }

    public LiveData<Boolean> login(String name, String password){
        return repository.login(name,password);
    }

    public LiveData<Boolean> createNewUser(String name, String password){
        return repository.createUser(name,password);
    }

}
