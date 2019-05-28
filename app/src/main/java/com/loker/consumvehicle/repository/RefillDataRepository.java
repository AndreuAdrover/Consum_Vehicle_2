package com.loker.consumvehicle.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RefillDataRepository {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private MutableLiveData<Boolean> Success = new MutableLiveData<>();
    private MutableLiveData<List<Refill>> carRefillsLD = new MutableLiveData<>();

    public RefillDataRepository() {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth!=null)
            user = firebaseAuth.getCurrentUser();
    }

    public LiveData<Boolean> addRefill(final Refill refill){

        db.collection("refills").add(refill)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Success.setValue(true);
                        //update LiveData List
                        List<Refill> addList = carRefillsLD.getValue();
                        addList.add(refill);
                        carRefillsLD.setValue(addList);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("addCar",e.getMessage());
                        Success.setValue(false);
                    }
                });

        return Success;

    }

    public LiveData<List<Refill>> getCarRefills(String carName){
        Query query;
        if(user!=null) {
            if (carName == null) {
                query = db.collection("refills").orderBy("date").whereEqualTo("uid", user.getUid());
            } else {
                query = db.collection("refills").orderBy("date").whereEqualTo("uid", user.getUid()).whereEqualTo("carName", carName);
            }

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Refill> refills = new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    refills.add(documentSnapshot.toObject(Refill.class));
                                }

                                carRefillsLD.setValue(refills);

                            } else {
                                Log.d("getCarRefill", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else{
            Log.d("RefillDataRepository","Error:user = null");
        }

        return carRefillsLD;

    }

    public void deleteCarRefills(String carName){
        db.collection("refills").whereEqualTo("uid",user.getUid())
                .whereEqualTo("carName",carName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<Refill> deleteSublist;
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                documentSnapshot.getReference().delete();
                            }

                        }else{
                            Log.d("deleteCarRefills", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public LiveData<Boolean> changeCarNameOfRefills(String carName, String oldName){
        db.collection("refills").whereEqualTo("uid",user.getUid())
                .whereEqualTo("carName",oldName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful()){
                          for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                            documentSnapshot.getReference().update("carName",carName);
                          }
                          Success.setValue(true);

                      }else{
                          Log.d("changeCarNameOfRefills", "Error getting documents: ", task.getException());
                          Success.setValue(false);
                      }
                    }
                });
        return Success;
    }
    public LiveData<Boolean> deleteRefill(final Refill refill){
        db.collection("refills")
                .whereEqualTo("uid",user.getUid())
                .whereEqualTo("carName",refill.getCarName())
                .whereEqualTo("date",refill.getDate())
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                documentSnapshot.getReference().delete();
                            }
                            Success.setValue(true);
                            //update LiveData List
                            List<Refill> deleteList = carRefillsLD.getValue();
                            deleteList.remove(refill);
                            carRefillsLD.setValue(deleteList);
                        }else{
                            Log.d("deleteRefill", "Error getting documents: ", task.getException());
                            Success.setValue(false);
                        }
                    }
                });

        return Success;
    }


}
