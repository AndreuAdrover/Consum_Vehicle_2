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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;
import com.loker.consumvehicle.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CarDataRepository {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private MutableLiveData<List<Car>> userCarListLD =new MutableLiveData<>();

    private MutableLiveData<Boolean> addCarSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteCarSucces = new MutableLiveData<>();


    public CarDataRepository() {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth!=null)
            user = firebaseAuth.getCurrentUser();
    }

    public LiveData<Boolean> addCar(Car newCar) {
       /* Map<String, Object> car = new HashMap<>();
        car.put("UID", newCar.getUID());
        car.put("name", newCar.getCarName());
        car.put("inicial_kms", newCar.getInicialKm());
        car.put("total_kms",newCar.getInicialKm());*/
        newCar.setTotalKm(newCar.getInicialKm());
        db.collection("cars").add(newCar)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        addCarSuccess.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("addCar",e.getMessage());
                        addCarSuccess.setValue(false);
                    }
                });

        return addCarSuccess;
    }

    public LiveData<List<Car>> getUserCarList(){
        if(user != null)
        db.collection("cars").whereEqualTo("uid",user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Car> userCarList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("getUserCarLis:Repot", document.getId() + " => " + document.getData());
                                /*Map map = document.getData();
                                Car car = new Car(Objects.requireNonNull(map.get("name")).toString()
                                        , Objects.requireNonNull(map.get("UID")).toString(),
                                        Float.parseFloat(Objects.requireNonNull(map.get("inicial_kms")).toString()),
                                        Float.parseFloat(Objects.requireNonNull(map.get("total_kms")).toString()));*/

                                userCarList.add(document.toObject(Car.class));
                            }
                            userCarListLD.setValue(userCarList);
                        } else {
                            Log.d("getUserCarList", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return userCarListLD;

    }

    public LiveData<Boolean> deleteCar(Car carToDelte) {

        db.collection("cars")
                .whereEqualTo("carName",carToDelte.getCarName())
                .whereEqualTo("uid",carToDelte.getUID())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult())
                        document.getReference().delete();
                    deleteCarSucces.setValue(true);
                }else{
                    deleteCarSucces.setValue(false);
                }
            }
        });

        return deleteCarSucces;
    }


}
