package com.loker.consumvehicle.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;

import java.util.List;

public class CarsListAdapter extends RecyclerView.Adapter<CarsListAdapter.CarHolder> {

    public static final String CAR_CLICKED = "com.loker.consumvehicle.activities.ApaterCarList.CAR_CLICKED";

    private Context ctx;
    private List<Car> carList;
    private List<Refill> refillList;

    public CarsListAdapter(Context ctx, List<Car> carList, List<Refill> refills) {
        this.ctx = ctx;
        this.carList = carList;
        this.refillList = refills;
    }

    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.car,viewGroup,false);

        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarHolder carHolder, int i) {

        final Car car = carList.get(i);

        carHolder.tvCarName.setText(car.getCarName());
        carHolder.tvKmsOnClock.setText(String.valueOf(car.getTotalKm()));
        carHolder.tvKmsLogged.setText(String.valueOf(car.calcKmsLogged()));
        carHolder.tvLiters.setText(String.valueOf(car.getTotalLitres(refillList)));
        carHolder.tvEuros100km.setText(String.valueOf(car.getAVGEuros(refillList)));
        carHolder.tvEurosTotal.setText(String.valueOf(car.getTotalEuros(refillList)));
        carHolder.tvLitres100km.setText(String.valueOf(car.getAVGLitres(refillList)));

        if(car.getBitmapImageCar()!=null){
            carHolder.imageCar.setVisibility(View.VISIBLE);
            carHolder.imageCar.setImageBitmap(car.getBitmapImageCar());
        }else{
            carHolder.imageCar.setVisibility(View.INVISIBLE);
        }


        carHolder.setItemCarListClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent= new Intent(ctx, RefillsActivity.class);
                car.setBitmapImageCar(null);
                intent.putExtra(CAR_CLICKED,new Gson().toJson(car));
                //intent.putExtra(CAR_CLICKED,car.getCarName());
                //intent.putExtra(MainActivity.APPUSER_UID,car.getUID());
                ctx.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void setCarList(List<Car> carsList,List<Refill> refills){
        this.carList =carsList;
        this.refillList = refills;
        notifyDataSetChanged();

    }
    public  void setImageCars(Car car,int postition){
        this.carList.get(postition).setBitmapImageCar(car.getBitmapImageCar());
        notifyItemChanged(postition);
    }


    class CarHolder extends RecyclerView.ViewHolder{

        TextView tvCarName;
        TextView tvKmsOnClock;
        TextView tvKmsLogged;
        TextView tvLiters;
        TextView tvEurosTotal;
        TextView tvEuros100km;
        TextView tvLitres100km;
        ImageView imageCar;

        ConstraintLayout layout;

        ItemClickListener itemCarListClickListener;
        ItemClickListener imageCarClickListener;

        public CarHolder(@NonNull View itemView) {
            super(itemView);

            tvCarName = itemView.findViewById(R.id.tv_carName);
            tvKmsOnClock = itemView.findViewById(R.id.tv_kms);
            tvLiters = itemView.findViewById(R.id.tv_litres);
            tvLitres100km = itemView.findViewById(R.id.tv_Lto100km);
            tvKmsLogged = itemView.findViewById(R.id.tv_kms_noted);
            tvEurosTotal = itemView.findViewById(R.id.tv_euros);
            tvEuros100km = itemView.findViewById(R.id.tv_euros100km);
            //icItemMenu = itemView.findViewById(R.id.ic_itemMenu);
            layout = itemView.findViewById(R.id.car_layout);
            imageCar = itemView.findViewById(R.id.imageCar);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(ctx,"Anar a refill list", Toast.LENGTH_SHORT).show();
                    itemCarListClickListener.onItemClickListener(view,getLayoutPosition());
                }
            });
        }

        private void setItemCarListClickListener(ItemClickListener ic){
            this.itemCarListClickListener = ic;
        }


    }

    /*private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }*/

}
