package com.loker.consumvehicle.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.loker.consumvehicle.activities.CarsListAdapter.CAR_CLICKED;

public class AddRefillActivity extends AppCompatActivity {

    public static final String NEW_REFILL = "com.loker.consumvehicle.activities.AddRefillActivity.RefillObj";

    private EditText etLitres,etKms,etPrice;
    private RadioGroup rbgFuelType;
    private TextView tvInputDate;

    private Refill newRefill = new Refill();
    private Car currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_refill);

        Intent intent = getIntent();
        if(intent.getStringExtra(CAR_CLICKED)!=null){
            currentCar = new Gson().fromJson(intent.getStringExtra(CAR_CLICKED), Car.class);
            newRefill.setUid(currentCar.getUID());
            newRefill.setCarName(currentCar.getCarName());
            setTitle(getString(R.string.title_activity_add_refill)+" "+newRefill.getCarName());
            //set the current date
            tvInputDate = findViewById(R.id.et_date);
            newRefill.setDate(new Date(System.currentTimeMillis()));
            tvInputDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newRefill.getDate()));

            etLitres = findViewById(R.id.et_litres);
            etKms = findViewById(R.id.et_kms);
            etPrice = findViewById(R.id.et_price);
            rbgFuelType = findViewById(R.id.radioGroup);


        }else{
                Log.d("AddRefillActivity", "Error: intent.getStringExtra(CAR_CLICKED)==null");
                finish();
        }


    }

    public void addRefill(View view) {
       //get values from view
        int checked = rbgFuelType.getCheckedRadioButtonId();
        RadioButton radi = findViewById(checked);
        newRefill.setFuelType(radi.getText().toString());
        newRefill.setPrice(Float.parseFloat(etPrice.getText().toString()));
        newRefill.setKms(Float.parseFloat(etKms.getText().toString()));
        newRefill.setLitres(Float.parseFloat(etLitres.getText().toString()));
        if(newRefill.getKms()>=currentCar.getInicialKm()){
        //Toast.makeText(this,radi.getText().toString(),Toast.LENGTH_SHORT).show();
         Gson gson = new Gson();
         Intent intent = new Intent();
         intent.putExtra(NEW_REFILL,gson.toJson(newRefill));
         setResult(RESULT_OK,intent);
         finish();
        }else{
            Toast.makeText(this,"Kms on Clock can't be less than inicial kms",Toast.LENGTH_SHORT).show();
        }

    }

    public void setDate(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog refillDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                tvInputDate.setText(day+"-"+(month+1)+"-"+year);
                Calendar now = Calendar.getInstance();
                now.set(year,month,day);
                newRefill.setDate(now.getTime());
                Toast.makeText(getBaseContext(),new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newRefill.getDate()),Toast.LENGTH_SHORT).show();


            }
        },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        refillDate.show();

    }

    public void finish(View view) {
        finish();
    }
}
