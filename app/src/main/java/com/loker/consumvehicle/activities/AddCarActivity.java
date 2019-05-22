package com.loker.consumvehicle.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Car;

import java.io.ByteArrayOutputStream;

public class AddCarActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText etTankLitres, etKms, etCarName;

    private ImageView imageCar;

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
        etTankLitres = findViewById(R.id.et_litres_deposit);
        imageCar =  findViewById(R.id.imageCar);

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
         newCar.setTankLitres(Float.parseFloat(etTankLitres.getText().toString()));
        if(!etKms.getText().toString().isEmpty()) {
            newCar.setInicialKm(Float.parseFloat(String.valueOf(etKms.getText())));
            newCar.setTotalKm(newCar.getInicialKm());
        }
        if(!newCar.getCarName().isEmpty()){
            if(newCar.getInicialKm() >= 0){
                Intent reply = new Intent();
                reply.putExtra("carName",newCar.getCarName());
                reply.putExtra("inicialKms",newCar.getInicialKm());
                reply.putExtra("tankLitres",newCar.getTankLitres());
                if(newCar.getBitmapImageCar()!=null)
                    reply.putExtra("thumbnail",getStringFromBitmap(newCar.getBitmapImageCar()));
                setResult(RESULT_OK,reply);
                finish();

            }else{
                Toast.makeText(this,getString(R.string.invalid_kms_value),Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,getString(R.string.invalid_name_car),Toast.LENGTH_SHORT).show();
        }

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
    private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
}
