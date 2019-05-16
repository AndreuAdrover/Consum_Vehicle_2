package com.loker.consumvehicle.activities;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.loker.consumvehicle.R;
import com.loker.consumvehicle.ViewModel.RefillDataViewModel;
import com.loker.consumvehicle.model.Refill;

import java.util.ArrayList;
import java.util.List;


public class RefillsActivity extends AppCompatActivity {

    public static final String CAR_NAME = "com.loker.consumvehicle.activities.RefillsActivity.carName";
    private static final int REQUEST_ADD_REFILL = 0;

    private RefillDataViewModel refillDataViewModel;

    private List<Refill> carRefills = new ArrayList<>();

    private RecyclerView carRefillsRV;
    private AdapterRefillList carRefillsAdapter;

    private String carName;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_list);


        Intent intent = getIntent();
        if(intent.getStringExtra(AdapterCarsList.CAR_CLICKED)!=null){

            carName = intent.getStringExtra(AdapterCarsList.CAR_CLICKED);
            uid = intent.getStringExtra(MainActivity.APPUSER_UID);

            setTitle(getString(R.string.title_activity_refill_list)+" "+carName);

            carRefillsRV = findViewById(R.id.llista);
            carRefillsRV.setHasFixedSize(true);
            carRefillsRV.setLayoutManager(new LinearLayoutManager(this));

            carRefillsAdapter=new AdapterRefillList(this,carRefills);
            carRefillsRV.setAdapter(carRefillsAdapter);


            refillDataViewModel = ViewModelProviders.of(this).get(RefillDataViewModel.class);
            refillDataViewModel.getCarRefills(carName).observe(this, new Observer<List<Refill>>() {
                @Override
                public void onChanged(@Nullable List<Refill> refills) {
                    carRefills = refills;
                    carRefillsAdapter.setCarRefills(refills);
                }
            });
        }else{
            Toast.makeText(this,"Error starting RefillsList Activity",Toast.LENGTH_SHORT).show();
            finish();
        }




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefillsActivity.this,AddRefillActivity.class);
                intent.putExtra(CAR_NAME,carName);
                intent.putExtra(MainActivity.APPUSER_UID,uid);
                startActivityForResult(intent,REQUEST_ADD_REFILL);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ADD_REFILL){
            if(resultCode == RESULT_OK){
                Refill refill = new Refill();

                Toast.makeText(this,"Adding refill",Toast.LENGTH_SHORT).show();

            }
        }

    }
}




