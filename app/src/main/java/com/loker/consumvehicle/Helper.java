package com.loker.consumvehicle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;

import java.io.ByteArrayOutputStream;
import java.util.List;

public final class Helper {
    private Helper(){
        throw new IllegalStateException("No instances.");
    }

    public static String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
    public static Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    //scale the image using the fractionOfScreen value as a width reference
    public static Bitmap scaleBitmap(Activity act, Bitmap imageBitmap, float fractionOfScreen){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width =displayMetrics.widthPixels * fractionOfScreen;
        float nh =  imageBitmap.getHeight() * (width / imageBitmap.getWidth());
        Log.d("width, height,widthPxls", String.valueOf(width)+","+nh+","+displayMetrics.widthPixels);
        return Bitmap.createScaledBitmap(imageBitmap, (int)width, Math.round(nh), true);
    }

    public static int findCarIndex(List<Car> carList, CheckCar tester){
        int index = 0;
        for(Car c: carList){
            if(tester.test(c)){
                return index;
            }
            index++;

        }
        return -1;
    }

    public static Car findCar(List<Car> cars, CheckCar tester){
        for (Car c: cars){
            if(tester.test(c)){
                return c;
            }
        }
        return null;
    }

    public static int  findRefillIndex(List<Refill> refills, CheckRefill tester){

        int i = 0;
        for(Refill r: refills){
            if(tester.test(r)){
                return i;
            }
            i++;
        }

        return -1;
    }

    public static Refill findRefill(List<Refill> refills, CheckRefill tester){

        for(Refill r: refills){
            if(tester.test(r)){
                return r;
            }
        }

        return null;
    }

    public static interface CheckCar {

        boolean test(Car car);
    }

    public static interface CheckRefill {

        boolean test(Refill refill);
    }
}
