package com.loker.consumvehicle.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;

import java.util.ArrayList;
import java.util.List;

public class RefillDataRepository {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private MutableLiveData<Boolean> addRefillSuccess = new MutableLiveData<>();
    private MutableLiveData<List<Refill>> carRefillsLD = new MutableLiveData<>();

    public RefillDataRepository() {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth!=null)
            user = firebaseAuth.getCurrentUser();
    }

    public LiveData<Boolean> addRefill(Refill refill){

        db.collection("refills").add(refill)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        addRefillSuccess.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("addCar",e.getMessage());
                        addRefillSuccess.setValue(false);
                    }
                });

        return addRefillSuccess;

    }

    public LiveData<List<Refill>> getCarRefills(String carName){

        db.collection("refills")
                .whereEqualTo("carName",carName)
                .whereEqualTo("uid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<Refill> refills = new ArrayList<>();
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                refills.add(documentSnapshot.toObject(Refill.class));
                            }
                            carRefillsLD.setValue(refills);

                        } else {
                            Log.d("getCarRefill", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return carRefillsLD;

    }
}
