package com.loker.consumvehicle.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loker.consumvehicle.Helper;
import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Car;

import java.io.ByteArrayOutputStream;

public class EditCarActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText etCarName;

    private ImageView imageCar = null;

    private Car newCar = new Car();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        etCarName = findViewById(R.id.et_carName);
        imageCar =  findViewById(R.id.imageCar);

        Intent intent = getIntent();
        if(intent.getStringExtra("car")!=null) {
            newCar=new Gson().fromJson(intent.getStringExtra("car"),Car.class);
            etCarName.setText(newCar.getCarName());
            if(intent.getStringExtra("imageCar")!=null) {
                imageCar.setImageBitmap(new Helper().getBitmapFromString(intent.getStringExtra("imageCar")));
            }

        }else{
            Toast.makeText(this,"Authentication error",Toast.LENGTH_SHORT).show();
            finish();
        }

        //carDataViewModel = ViewModelProviders.of(this).get(CarDataViewModel.class);
    }

    public void editCar(View view) {

                Intent reply = new Intent();
                if(!etCarName.getText().toString().equals(newCar.getCarName())){
                    reply.putExtra("oldCarName",newCar.getCarName());
                }
                reply.putExtra("carName",etCarName.getText().toString());
                if(newCar.getBitmapImageCar()!=null)//it means we have changed the image
                    reply.putExtra("thumbnail",new Helper().getStringFromBitmap(newCar.getBitmapImageCar()));
                setResult(RESULT_OK,reply);
                finish();

    }

    public void addImageCar(View view) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageCar.setImageBitmap(imageBitmap);
            newCar.setBitmapImageCar(imageBitmap);

        }
    }

}
