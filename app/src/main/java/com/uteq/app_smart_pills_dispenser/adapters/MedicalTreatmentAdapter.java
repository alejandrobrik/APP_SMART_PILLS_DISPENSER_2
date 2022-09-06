package com.uteq.app_smart_pills_dispenser.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MedicalTreatmentAdapter extends RecyclerView.Adapter<MedicalTreatmentAdapter.MedicalTreatmentViewHolder> {

    private Dosage dosage;

    private List<MedicalTreatment> data = new ArrayList<>();
    private List<MedicalTreatment> originalData = new ArrayList<>();
    private Context context;


    public MedicalTreatmentAdapter() {
    }

    public void setData(List<MedicalTreatment> data) {
        this.data = data;
        this.originalData.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<MedicalTreatment> data, Dosage dosage) {
        this.data = data;
        this.originalData.addAll(data);
        this.dosage = dosage;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MedicalTreatmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MedicalTreatmentViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_medicaltreatment, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MedicalTreatmentViewHolder holder, int position) {
        MedicalTreatment medicalTreatment = data.get(position);

        holder.tvDescription.setText(MoreUtils.coalesce(medicalTreatment.getDescription(), "N/D"));
        holder.tvStartDate.setText(MoreUtils.coalesce(medicalTreatment.getStart_Date(), "N/D"));
        holder.tvEndDate.setText(MoreUtils.coalesce(medicalTreatment.getEndDate(), "N/D"));


/*        if(this.dosage != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putSerializable("treatment", new Gson().toJson(data.get(holder.getAdapterPosition())));
                    b.putSerializable("dosage",dosage);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Navigation.findNavController(view).navigate(R.id.dosageAddFragment, b);
                }
            });
        } else {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putSerializable("patient", new Gson().toJson(data.get(holder.getAdapterPosition())));

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Navigation.findNavController(view).navigate(R.id.patientMenuFragment, b);
                }
            });
        }*/

    }


    public int getItemCount() {
        return data.size();
    }

    public void filter(@NonNull String strSearch) {

        //final  List<Patient> original = this.originalData;

        if (strSearch.length() == 0) {

            this.data.clear();
            this.data.addAll(this.originalData);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                List<MedicalTreatment> collect = this.originalData.stream()
                        .filter(p -> p.getDescription().toLowerCase().contains(strSearch.toLowerCase()))
                        .collect(Collectors.toList());
                this.data.clear();
                data.addAll(collect);
            } else {
                for (MedicalTreatment p : originalData) {
                    if (p.getDescription().toLowerCase().contains(strSearch.toLowerCase())) {
                        data.add(p);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }

    public class MedicalTreatmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription;
        TextView tvStartDate;
        TextView tvEndDate;
        ImageView imgEvaluador;
        CardView cardView;


        public MedicalTreatmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvTreatmentDescription);
            tvStartDate = itemView.findViewById(R.id.tvTreatmentStarDate);
            tvEndDate = itemView.findViewById(R.id.tvTreatmentEndDate);
            imgEvaluador = itemView.findViewById(R.id.imgEvaluador);
            cardView = itemView.findViewById(R.id.cardViewMainMedicalTreatment);
        }


    }

}