package com.loker.consumvehicle.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES10;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.loker.consumvehicle.Helper;
import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Car;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddCarActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

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
                    reply.putExtra("thumbnail",Helper.getStringFromBitmap(newCar.getBitmapImageCar()));
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

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGaleri();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();

    }
    public void openGaleri(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                REQUEST_IMAGE_GALLERY);
    }
    public void takePhotoFromCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage;
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
           selectedImage = data.getData();
           if(selectedImage.getPath()!=null){
               InputStream imageStream = null;
               try {
                   imageStream = getContentResolver().openInputStream(selectedImage);
               }catch (FileNotFoundException e){
                   e.printStackTrace();
               }

               Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
               //scale image to fit in the card
               Bitmap scaled = Helper.scaleBitmap(this,imageBitmap,(float)1/3);

               imageCar.setImageBitmap(scaled);
               newCar.setBitmapImageCar(scaled);
           }


        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageCar.setImageBitmap(imageBitmap);
            newCar.setBitmapImageCar(imageBitmap);

        }
    }


}
