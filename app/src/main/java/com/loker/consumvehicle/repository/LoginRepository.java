package com.loker.consumvehicle.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loker.consumvehicle.model.User;


public class LoginRepository {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> createSuccess = new MutableLiveData<>();

    public LoginRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //createSuccess.setValue(false);
    }

    public LiveData<User> getUser(){
        MutableLiveData<User> currentUser = new MutableLiveData<>();
        if(user == null){
            currentUser.setValue(null);
        }else {
            currentUser.setValue(new User(user.getUid(),user.getEmail()));
            //get information from users document.....
        }
        return currentUser;

    }

    public void signOut(){
        firebaseAuth.signOut();
    }

    public LiveData<Boolean> login(String name, String password){
        firebaseAuth.signInWithEmailAndPassword(name, password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       //if the task is successfull
                       if(task.isSuccessful()){
                           loginSuccess.setValue(true);
                       }else{
                           loginSuccess.setValue(false);
                       }
                   }
               });
        return loginSuccess;

    }

    public LiveData<Boolean> createUser(String userName, String password){

        firebaseAuth.createUserWithEmailAndPassword(userName,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                           createSuccess.setValue(true);

                        }else{
                            createSuccess.setValue(false);
                            Log.d("createUser",task.getResult().toString());
                        }

                    }
                });

        return createSuccess;
    }


}
