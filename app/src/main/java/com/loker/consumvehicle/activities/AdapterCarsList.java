package com.loker.consumvehicle.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Car;

import java.util.List;

public class AdapterCarsList extends RecyclerView.Adapter<AdapterCarsList.CarHolder> {

    public static final String CAR_CLICKED = "com.loker.consumvehicle.activities.ApaterCarList.CAR_CLICKED";

    private Context ctx;
    private List<Car> carList;

    public AdapterCarsList(Context ctx, List<Car> carList) {
        this.ctx = ctx;
        this.carList = carList;
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

        carHolder.setItemCarListClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent= new Intent(ctx, RefillsActivity.class);
                intent.putExtra(CAR_CLICKED,car.getCarName());
                intent.putExtra(MainActivity.APPUSER_UID,car.getUID());
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void setCarList(List<Car> carsList){
        this.carList =carsList;
        notifyDataSetChanged();

    }


    class CarHolder extends RecyclerView.ViewHolder{

        TextView tvCarName;
        TextView tvKmsOnClock;
        TextView icItemMenu;

        ConstraintLayout layout;

        ItemClickListener itemCarListClickListener;

        public CarHolder(@NonNull View itemView) {
            super(itemView);

            tvCarName = itemView.findViewById(R.id.tv_carName);
            tvKmsOnClock = itemView.findViewById(R.id.tv_kms);
            icItemMenu = itemView.findViewById(R.id.ic_itemMenu);
            layout = itemView.findViewById(R.id.car_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(ctx,"Anar a refill list", Toast.LENGTH_SHORT).show();
                    itemCarListClickListener.onItemClickListener(view,getLayoutPosition());
                }
            });
        }

        public void setItemCarListClickListener(ItemClickListener ic){
            this.itemCarListClickListener = ic;
        }
    }

}
