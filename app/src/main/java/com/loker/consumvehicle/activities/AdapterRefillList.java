package com.loker.consumvehicle.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Refill;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdapterRefillList extends RecyclerView.Adapter<AdapterRefillList.RefillHolder> {

    Context ctx;
    List<Refill> carRefillList;

    public AdapterRefillList(Context ctx, List<Refill> carRefillList) {
        this.ctx = ctx;
        this.carRefillList = carRefillList;
    }

    @NonNull
    @Override
    public RefillHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.refill,viewGroup, false);
        return new RefillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefillHolder refillHolder, int i) {

        Refill refill = carRefillList.get(i);

        refillHolder.tvPrice.setText(String.valueOf(refill.getPrice()));
        refillHolder.tvDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(refill.getDate()));
        refillHolder.tvTypeFuel.setText(refill.getFuelType());
        refillHolder.tvLitres.setText(String.valueOf(refill.getLitres()));
        refillHolder.tvKms.setText(String.valueOf(refill.getKms()));

    }

    @Override
    public int getItemCount() {
        return carRefillList.size();
    }

    public void setCarRefills(List<Refill> refills){
        this.carRefillList=refills;
        notifyDataSetChanged();
    }


    class RefillHolder extends RecyclerView.ViewHolder{

        TextView tvDate, tvKms, tvLitres, tvTypeFuel, tvPrice;


        public RefillHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvKms = itemView.findViewById(R.id.tv_kms);
            tvTypeFuel = itemView.findViewById(R.id.tv_fuel_type);
            tvLitres = itemView.findViewById(R.id.tv_litres);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
