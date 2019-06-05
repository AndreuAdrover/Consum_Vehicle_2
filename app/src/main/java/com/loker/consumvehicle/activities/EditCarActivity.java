package com.loker.consumvehicle.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

public class EditCarActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

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
                imageCar.setImageBitmap(Helper.getBitmapFromString(intent.getStringExtra("imageCar")));
            }

        }else{
            Toast.makeText(this,"Authentication error",Toast.LENGTH_SHORT).show();
            finish();
        }

        //carDataViewModel = ViewModelProviders.of(this).get(CarDataViewModel.class);
    }

    public void editCar(View view) {

                Intent reply = new Intent();
                //check if the name has been changed
                if(!etCarName.getText().toString().equals(newCar.getCarName())){
                    //if the name hase been changed we send oldCarName value
                    reply.putExtra("oldCarName",newCar.getCarName());
                }
                //send actual carName
                reply.putExtra("carName",etCarName.getText().toString());

                if(newCar.getBitmapImageCar()!=null)//it means we have changed the image
                    //send the new image in String format
                    reply.putExtra("thumbnail",Helper.getStringFromBitmap(newCar.getBitmapImageCar()));
                setResult(RESULT_OK,reply);
                finish();

    }

    public void addImageCar(View view) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                getString(R.string.select_from_gallery),
                getString(R.string.take_a_photo_with_camera) };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();

    }

    public void openGallery(){
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
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageCar.setImageBitmap(imageBitmap);
                newCar.setBitmapImageCar(imageBitmap);

            }
            else if(requestCode == REQUEST_IMAGE_GALLERY){
                Uri selectedImage;
                selectedImage = data.getData();
                if(selectedImage.getPath()!=null) {
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                    //scale image to fit in the card
                    Bitmap scaled = Helper.scaleBitmap(this, imageBitmap, (float) 1 / 3);

                    imageCar.setImageBitmap(scaled);
                    newCar.setBitmapImageCar(scaled);
                }
            }
        }
    }

}
