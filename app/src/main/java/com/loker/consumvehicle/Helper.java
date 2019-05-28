package com.loker.consumvehicle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.CheckCar;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Helper {

    public String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
    public Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public int findCarIndex(List<Car> carList, CheckCar tester){
        int index = 0;
        for(Car c: carList){
            if(tester.test(c)){
                return index;
            }
            index++;

        }
        return -1;
    }
}
