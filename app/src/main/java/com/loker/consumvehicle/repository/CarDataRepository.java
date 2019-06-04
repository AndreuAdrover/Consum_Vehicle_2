package com.loker.consumvehicle.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loker.consumvehicle.Helper;
import com.loker.consumvehicle.model.Car;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CarDataRepository {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private MutableLiveData<List<Car>> userCarListLD =new MutableLiveData<>();

    private MutableLiveData<Boolean> addCarSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteCarSucces = new MutableLiveData<>();


    public CarDataRepository() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth!=null)
            user = firebaseAuth.getCurrentUser();
    }

    public LiveData<Boolean> addCar(final Car newCar) {
       /* Map<String, Object> car = new HashMap<>();
        car.put("UID", newCar.getUID());
        car.put("name", newCar.getCarName());
        car.put("inicial_kms", newCar.getInicialKm());
        car.put("total_kms",newCar.getInicialKm());*/
        newCar.setTotalKm(newCar.getInicialKm());
        //newCar.setUrlImageCar(storage.getReference().getBucket()+"/"+newCar.getCarName()+"_img");

        if(newCar.getBitmapImageCar()!=null) {

            //upload the image Car
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newCar.getBitmapImageCar().compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference imageCarRef = storage.getReference().child(newCar.getCarName() + "_img");
            UploadTask uploadTask = imageCarRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    addCarSuccess.setValue(false);
                    Log.d("addCarImageSuccesError:",exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //if the upload is successful get the url for storage Firebase and update the url field in the DB
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("URL_prova",uri.toString());
                            newCar.setUrlImageCar(uri.toString());
                            db.collection("cars").add(newCar)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            //notify the adding was a succes
                                            addCarSuccess.setValue(true);
                                            //add the new car to the user car list to trigger the onChange()
                                            List<Car> addList = userCarListLD.getValue();
                                            addList.add(newCar);
                                            userCarListLD.setValue(addList);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("addCar",e.getMessage());
                                            addCarSuccess.setValue(false);
                                        }
                                    });
                        }
                    });

                }
            });
        }else{
            db.collection("cars").add(newCar)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //notify the adding was a succes
                            addCarSuccess.setValue(true);
                            //add the new car to the user car list to trigger the onChange()
                            List<Car> addList = userCarListLD.getValue();
                            addList.add(newCar);
                            userCarListLD.setValue(addList);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("addCar",e.getMessage());
                            addCarSuccess.setValue(false);
                        }
                    });
        }


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
                                Car car = document.toObject(Car.class);

                                userCarList.add(car);

                            }
                            userCarListLD.setValue(userCarList);
                        } else {
                            Log.d("getUserCarList", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return userCarListLD;

    }

    public LiveData<Boolean> deleteCar(final Car carToDelte) {

        db.collection("cars")
                .whereEqualTo("carName",carToDelte.getCarName())
                .whereEqualTo("uid",carToDelte.getUID())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        document.getReference().delete();
                        if(carToDelte.getUrlImageCar()!=null){
                            //if there is an image for this car in the storage we have to remove it
                            //the deleteImageCar funciton sets deleteCarSucces to true if works
                            deleteImageCar(carToDelte.getUrlImageCar());
                        }else{
                            deleteCarSucces.setValue(true);

                        }
                    }
                    //modify userCarList LiveData to trigger the onChange() in the observer
                    List<Car> deleteList = userCarListLD.getValue();
                    deleteList.remove(carToDelte);

                }else{
                    deleteCarSucces.setValue(false);
                }
            }
        });

        return deleteCarSucces;
    }


    public void updateCar(final Car car){
        db.collection("cars")
                .whereEqualTo("carName",car.getCarName())
                .whereEqualTo("uid",car.getUID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                documentSnapshot.getReference().set(car);
                                addCarSuccess.setValue(true);
                                //update userCarListLD;
                                List<Car> updateList = userCarListLD.getValue();
                                int index = Helper.findCarIndex(updateList,
                                        (Car c)->c.getCarName().equals(car.getCarName()) && c.getUID().equals(car.getUID()));
                                updateList.set(index,car);
                                userCarListLD.setValue(updateList);
                            }

                        }else{
                            Log.e("updateCar","Error updating car task:"+task.getResult().toString());
                        }
                    }
                });
    }

    public void updateCarName(Car car, String oldName){
        //remove urlImageCarReference because will be incorrect if we change the name
        car.setUrlImageCar(null);
        //find the car in the database using the oldname
        db.collection("cars").whereEqualTo("carName",oldName)
                .whereEqualTo("uid",user.getUid())
                .limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        doc.getReference().update("carName",car.getCarName());
                    }
                    List<Car> updateList = userCarListLD.getValue();
                    int index = Helper.findCarIndex(updateList,
                            (Car c)->oldName.equals(c.getCarName())&& user.getUid().equals(c.getUID()));
                    if(index>=0) {
                        updateList.set(index, car);
                        userCarListLD.setValue(updateList);
                    }else Log.d("updateCarName","Error car not found in the list");
                }else{
                    Log.e("updateCarName","Error updating car task:"+task.getResult().toString());
                }
            }
        });

    }

    public void updateImageCar(Car newCar){

        //upload the image Car
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newCar.getBitmapImageCar().compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imageCarRef = storage.getReference().child(newCar.getCarName() + "_img");
        UploadTask uploadTask = imageCarRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                addCarSuccess.setValue(false);
                Log.d("addCarImageSuccesError:",exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //if the upload is successful get the url for storage Firebase and update the url field in the DB
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("URL_prova",uri.toString());
                        newCar.setUrlImageCar(uri.toString());
                        updateCar(newCar);
                    }
                });

            }
        });

    }

    public LiveData<Bitmap> getCarImage(Car car){
        final MutableLiveData<Bitmap> bitmapLD= new MutableLiveData<>();
        if(car.getUrlImageCar()!=null){
            StorageReference imageRef=storage.getReferenceFromUrl(car.getUrlImageCar());
            imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    Bitmap bitmap = BitmapFactory.decodeStream(bais);
                    bitmapLD.setValue(bitmap);
                }
            });

        }
        return bitmapLD;
    }

    public void deleteImageCar(String url){
        StorageReference imageRef = storage.getReferenceFromUrl(url);
        imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    deleteCarSucces.setValue(true);
                }
            }
        });

    }




}
