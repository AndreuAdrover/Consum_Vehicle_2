package com.loker.consumvehicle.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Refill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RefillListAdapter extends RecyclerView.Adapter<RefillListAdapter.RefillHolder> {

    private Context ctx;
    private List<Refill> carRefillList;
    private List<Refill> checkedToRemoveRefills=new ArrayList<>();
    private boolean removeing = false;
    private boolean allChecked = false;

    public RefillListAdapter(Context ctx, List<Refill> carRefillList) {
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
    public void onBindViewHolder(@NonNull final RefillHolder refillHolder, int i) {

        final Refill refill = carRefillList.get(i);

        //Log.d("AdapterRefill","Refill:"+new Gson().toJson(refill));
        refillHolder.tvPrice.setText(String.valueOf(refill.getPrice()));
        refillHolder.tvDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(refill.getDate()));
        refillHolder.tvTypeFuel.setText(refill.getFuelType());
        refillHolder.tvLitres.setText(String.valueOf(refill.getLitres()));
        refillHolder.tvKms.setText(String.valueOf(refill.getKms()));
        refillHolder.removeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(refillHolder.removeCheck.isChecked())
                    checkedToRemoveRefills.add(refill);
                else
                    checkedToRemoveRefills.remove(refill);
            }
        });

        if(removeing){
            refillHolder.removeCheck.setVisibility(View.VISIBLE);
            if(allChecked){
                refillHolder.removeCheck.setChecked(true);
            }else{
                refillHolder.removeCheck.setChecked(false);
            }

        }else{
            refillHolder.removeCheck.setVisibility(View.GONE);

        }



    }

    @Override
    public int getItemCount() {
        return carRefillList.size();
    }

    public void setCarRefills(List<Refill> refills){
        this.carRefillList=refills;
        notifyDataSetChanged();
    }
    public void removingRefills(boolean b){
        this.removeing = b;
        notifyDataSetChanged();
    }

    public void setAllChecked(Boolean b){
        this.allChecked = b;
        notifyDataSetChanged();
    }
    public List<Refill> getChecked(){
        return checkedToRemoveRefills;
    }


    class RefillHolder extends RecyclerView.ViewHolder{

        TextView tvDate, tvKms, tvLitres, tvTypeFuel, tvPrice;
        CheckBox removeCheck;
        ItemClickListener itemCheckListener;


        public RefillHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvKms = itemView.findViewById(R.id.tv_KmOnClock);
            tvTypeFuel = itemView.findViewById(R.id.tv_fuel_type);
            tvLitres = itemView.findViewById(R.id.tv_litres);
            tvPrice = itemView.findViewById(R.id.tv_price);

            removeCheck = itemView.findViewById(R.id.remove_check);
        }


    }
}
