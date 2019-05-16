package com.loker.consumvehicle.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.loker.consumvehicle.R;
import com.loker.consumvehicle.ViewModel.CarDataViewModel;
import com.loker.consumvehicle.model.Car;

public class AddCarActivity extends AppCompatActivity {

    private EditText etLitres, etKms, etCarName;

    private Car newCar = new Car();
   // private CarDataViewModel carDataViewModel;
    //private String carName;
    //private String UID;
    //private float kmsOnClock = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        etKms = findViewById(R.id.et_kms);
        etCarName = findViewById(R.id.et_carName);

        Intent intent = getIntent();
        if(intent.getStringExtra(MainActivity.APPUSER_UID)!=null) {
            newCar.setUID(intent.getStringExtra(MainActivity.APPUSER_UID));
        }else{
            Toast.makeText(this,"Authentication error",Toast.LENGTH_SHORT).show();
            finish();
        }

        //carDataViewModel = ViewModelProviders.of(this).get(CarDataViewModel.class);
    }

    public void addNewCar(View view) {
         newCar.setCarName(String.valueOf(etCarName.getText()));
        if(!etKms.getText().toString().isEmpty()) {
            newCar.setInicialKm(Float.parseFloat(String.valueOf(etKms.getText())));
            newCar.setTotalKm(newCar.getInicialKm());
        }
        if(!newCar.getCarName().isEmpty()){
            if(newCar.getInicialKm() >= 0){
                Intent reply = new Intent();
                reply.putExtra("carName",newCar.getCarName());
                reply.putExtra("inicialKms",newCar.getInicialKm());
                setResult(RESULT_OK,reply);
                finish();

            }else{
                Toast.makeText(this,getString(R.string.invalid_kms_value),Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,getString(R.string.invalid_name_car),Toast.LENGTH_SHORT).show();
        }

    }

   /* private void addCar(String UID,String carName,float kms,float litres){
        Map<String, Object> car = new HashMap<>();
        car.put("UID",UID);
        car.put("name", carName);
        car.put("kms", kms);
        car.put("litres", litres);

        db.collection("cars").add(car)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddCarActivity.this,"Car added",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCarActivity.this,"Error Writting the document",Toast.LENGTH_SHORT).show();
                    }
                });

    }*/
}
