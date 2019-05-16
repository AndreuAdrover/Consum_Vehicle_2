package com.loker.consumvehicle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Refill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddRefillActivity extends AppCompatActivity {

    private EditText etLitres,etKms,etPrice;
    private RadioGroup rbgFuelType;
    private TextView tvInputDate;

    private Refill newRefill = new Refill();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_refill);

        Intent intent = getIntent();
        if(intent.getStringExtra(MainActivity.APPUSER_UID)!=null){
            newRefill.setUid(intent.getStringExtra(MainActivity.APPUSER_UID));
            if(intent.getStringExtra(RefillsActivity.CAR_NAME)!=null){
                newRefill.setCarName(intent.getStringExtra(RefillsActivity.CAR_NAME));
                setTitle(getString(R.string.title_activity_add_refill)+" "+newRefill.getCarName());
                //set the current date
                tvInputDate = findViewById(R.id.et_date);
                tvInputDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(System.currentTimeMillis())));

                etLitres = findViewById(R.id.et_litres);
                etKms = findViewById(R.id.et_kms);
                etPrice = findViewById(R.id.et_price);
                rbgFuelType = findViewById(R.id.radioGroup);
            }else{
                Log.d("AddRefillActivity","Error: intent.getStringExtra(RefillsActivity.CAR_NAME)==null");
                finish();
            }
        }else{
                Log.d("AddRefillActivity", "Error: intent.getStringExtra(MainActivity.APPUSER_UID)==null");
                finish();
        }


    }

    public void addRefill(View view) {
       //get the type of fuel from radioGRoup
        int checked = rbgFuelType.getCheckedRadioButtonId();
        RadioButton radi = findViewById(checked);



        Toast.makeText(this,radi.getText().toString(),Toast.LENGTH_SHORT).show();
       /* Intent intent = new Intent();
        setResult(RESULT_OK,intent);

        finish();*/
    }

    public void setDate(View view) {
        Toast.makeText(this,"datepicker",Toast.LENGTH_SHORT).show();
    }
}
