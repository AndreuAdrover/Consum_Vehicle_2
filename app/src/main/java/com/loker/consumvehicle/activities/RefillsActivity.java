package com.loker.consumvehicle.activities;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loker.consumvehicle.R;
import com.loker.consumvehicle.ViewModel.CarDataViewModel;
import com.loker.consumvehicle.ViewModel.RefillDataFactory;
import com.loker.consumvehicle.ViewModel.RefillDataViewModel;
import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.loker.consumvehicle.activities.AddRefillActivity.NEW_REFILL;
import static com.loker.consumvehicle.activities.CarsListAdapter.CAR_CLICKED;


public class RefillsActivity extends AppCompatActivity {

    public static final String CAR_NAME = "com.loker.consumvehicle.activities.RefillsActivity.carName";
    private static final int REQUEST_ADD_REFILL = 0;

    private RefillDataViewModel refillDataViewModel;
    private CarDataViewModel carDataViewModel;

    private List<Refill> carRefills = new ArrayList<>();

    private RecyclerView carRefillsRV;
    private RefillListAdapter carRefillsAdapter;

    String fileName;
    File path;

    //private String carName;
    //private String uid;
    private Car currentCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_list);

        Intent intent = getIntent();
        //if(intent.getStringExtra(CAR_CLICKED)!=null || currentCar!=null){

            //carName = intent.getStringExtra(CarsListAdapter.CAR_CLICKED);
            //uid = intent.getStringExtra(MainActivity.APPUSER_UID);
           // if(intent.getStringExtra(CAR_CLICKED)!=null)
            currentCar = new Gson().fromJson(intent.getStringExtra(CAR_CLICKED),Car.class);

            setTitle(getString(R.string.title_activity_refill_list)+" "+currentCar.getCarName());

            carRefillsRV = findViewById(R.id.llista);
            carRefillsRV.setHasFixedSize(true);
            carRefillsRV.setLayoutManager(new LinearLayoutManager(this));

            carRefillsAdapter=new RefillListAdapter(this,carRefills);
            carRefillsRV.setAdapter(carRefillsAdapter);

            carDataViewModel = ViewModelProviders.of(this).get(CarDataViewModel.class);
            refillDataViewModel = ViewModelProviders.of(this, new RefillDataFactory(getApplication(),currentCar.getCarName())).get(RefillDataViewModel.class);
            refillDataViewModel.getCarRefills(currentCar.getCarName()).observe(this, new Observer<List<Refill>>() {
                @Override
                public void onChanged(@Nullable List<Refill> refills) {
                    carRefills = refills;
                    carRefillsAdapter.setCarRefills(refills);
                }
            });
        /*}else{
            Toast.makeText(this,"Error starting RefillsList Activity",Toast.LENGTH_SHORT).show();
            finish();
        }*/




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefillsActivity.this,AddRefillActivity.class);
                intent.putExtra(CAR_CLICKED,new Gson().toJson(currentCar));
                /*intent.putExtra(CAR_NAME,currentCar.getCarName());
                intent.putExtra(MainActivity.APPUSER_UID,currentCar.getUID());*/
                startActivityForResult(intent,REQUEST_ADD_REFILL);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.refill_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_find:

                break;
            case R.id.action_export:
                exportRefills();
                break;
            case R.id.action_delete:
                removeRefills();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ADD_REFILL){
            if(resultCode == RESULT_OK){
                Refill refill = new Gson().fromJson(data.getStringExtra(NEW_REFILL),Refill.class);
                //check if it's the firs entry to the car refill list
                //if is not
                if(carRefills.size()>0) {
                    //check if the kms on clock are more than the last input

                    if (carRefills.get(carRefills.size() - 1).getKms() < refill.getKms()) {
                        //if the date is later the refill is correct
                        if (carRefills.get(carRefills.size() - 1).getDate().before(refill.getDate())) {
                            //update currentCar total kms
                            currentCar.setTotalKm(refill.getKms());
                            carDataViewModel.updateCar(currentCar);
                            //add refill
                            addRefill(refill);
                        } else {
                            Toast.makeText(this,
                                    "Values of refill incorrect: the date is before one that has more kms", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        //if kms ar lower that the current total kms of the
                        // car check the kms of the nearest entrance of te carRefill list to check the kms
                        for (Refill rf : carRefills) {
                            if (rf.getKms() > refill.getKms()) {
                                //check if the first refill with more kms than the actual refill was after the actual refill
                                if (rf.getDate().after(refill.getDate())) {
                                    //check if the refill before that one was before the acutal refill
                                    int index = carRefills.indexOf(rf);
                                    if (index > 0) {
                                        //if is not the first entry of the list
                                        if (carRefills.get(index - 1).getDate().before(refill.getDate()))
                                            addRefill(refill);
                                        else
                                            Toast.makeText(this,
                                                    "Values of refill incorrect: kms or date incorrect_2", Toast.LENGTH_LONG).show();
                                    } else {
                                        //if is the first entry check if the refill kms are at leats the same of the inicial kms of the car
                                        if(refill.getKms()>=currentCar.getInicialKm()){
                                            addRefill(refill);
                                        }else{
                                            Toast.makeText(this,
                                                    "Values of refill incorrect:" +
                                                            " You can't add refills from before adding the car to the app"
                                                    , Toast.LENGTH_LONG).show();

                                        }

                                    }
                                } else {
                                    Toast.makeText(this,
                                            "Values of refill incorrect: kms or date incorrect", Toast.LENGTH_LONG).show();
                                }
                                break;
                            }
                        }
                    }
                }else{//if it's the firs entry of the carREfill list
                    //check if the km value is bigger or at least the same than initial kms of the vehicle
                    if(currentCar.getInicialKm()<=refill.getKms()){
                        addRefill(refill);
                    }else{
                        Toast.makeText(this,
                                "First refill kms value should be at least the same of the initial kms value.",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        }

    }
    //add a refill to the carRefillList
    private void addRefill(Refill refill){
        //add refill to the list
        refillDataViewModel.
                addRefill(refill)
                .observe(RefillsActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if(aBoolean){
                            refillDataViewModel.resetSuccess();
                        }else{
                            Toast.makeText(RefillsActivity.this,"Error adding Refill",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void writeData(File path, String fileName){
        // Get the directory for the user's public pictures directory.
        boolean isPresent=true;
        if(!path.exists()){
            isPresent = path.mkdir();
        }
        Log.d("exportRefills()",path.toString()+"__"+path.getAbsolutePath());
        if(isPresent) {
            try {
                File myExternalFile = new File(path.getAbsolutePath(), fileName);

                FileWriter fos = new FileWriter(myExternalFile);
                int i=0;
                for(Refill rf: carRefills) {
                    fos.append(new Gson().toJson(rf, Refill.class));
                    i++;
                }
                Snackbar.make(findViewById(R.id.refillListCoordinatorLayout),i+" refills exported to "+path+"/"+fileName,Snackbar.LENGTH_LONG)
                        .show();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else{
            Toast.makeText(this, "External storage not available", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (Refill rf : carRefills) {
                        writeData(path, fileName);
                    }

                } else {
                    Toast.makeText(RefillsActivity.this, "Please provide permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void exportRefills() {
        fileName = currentCar.getCarName()+"Refills.json";
        path = new File(getExternalStorageDirectory() + "/Documents");
        if(!checkIfAlreadyhavePermission()){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            writeData(path,fileName);
        }
    }

    private void removeRefills(){
        carRefillsAdapter.removingRefills(true);
        findViewById(R.id.remove_layout).setVisibility(View.VISIBLE);

    }

    public void removeChecked(View view) {
        //Toast.makeText(this, "remove checked"+"num:"+carRefillsAdapter.getChecked().size(),Toast.LENGTH_SHORT).show();
        for(Refill refill: carRefillsAdapter.getChecked()){
            //if is the last refill we have to update the totalkms field of the car
            if(carRefills.indexOf(refill)==carRefills.size()-1){
                //if there is more thant one refill in the list
                Log.d("remove checked","kms of refill:"+refill.getKms());
                Log.d("remoce checked", "index of refill:"+carRefills.indexOf(refill));
                if(carRefills.size()>1) {
                    //get kms of the secont to last refillof the list
                    currentCar.setTotalKm(carRefills.get(carRefills.size() - 2).getKms());
                }
                else {
                    currentCar.setTotalKm(currentCar.getInicialKm());
                }

                carDataViewModel.updateCar(currentCar);
            }
            refillDataViewModel.deleteRefill(refill).observe(RefillsActivity.this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean aBoolean) {
                    //if the delete operation was succesfull
                    if(aBoolean){
                        refillDataViewModel.resetSuccess();

                    }else{
                        Toast.makeText(RefillsActivity.this,"Error deleting Refill",Toast.LENGTH_SHORT).show();

                    }
                }
            });
            //carRefills.remove(refill);
        }
        //carRefillsAdapter.setCarRefills(carRefills);
        carRefillsAdapter.removingRefills(false);
        findViewById(R.id.remove_layout).setVisibility(View.GONE);


    }

    public void checkAll(View view) {
        CheckBox checkAll = view.findViewById(R.id.check_all);
        carRefillsAdapter.setAllChecked(checkAll.isChecked());

    }
}




