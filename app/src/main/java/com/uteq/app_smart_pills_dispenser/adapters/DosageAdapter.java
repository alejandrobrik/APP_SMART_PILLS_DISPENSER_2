package com.uteq.app_smart_pills_dispenser.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Pill;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DosageAdapter extends RecyclerView.Adapter<DosageAdapter.DosageViewHolder> {
    private MedicalTreatment treatment;

    private List<Dosage> data = new ArrayList<>();
    private  List<Dosage> originalData = new ArrayList<>();
    private Context context;


    public DosageAdapter() {
    }

    public void setData(List<Dosage> data) {
        this.data = data;
        this.originalData.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<Dosage> data, MedicalTreatment treatment) {
        this.data = data;
        this.originalData.addAll(data);
        this.treatment = treatment;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DosageAdapter.DosageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DosageAdapter.DosageViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_dosage, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DosageAdapter.DosageViewHolder holder, int position) {

        Dosage dosage = data.get(position);

      //  String [] horario = dosage.getDate_hour().split("T");

        holder.tvQuantity.setText(MoreUtils.coalesce(String.valueOf(dosage.getQuantity()), "N/D"));
 //       holder.tvhoursDate.setText(MoreUtils.coalesce(horario[0] +"\n"+ horario[1].substring(0, 8), "N/D"));
        holder.tvhoursDate.setText(MoreUtils.coalesce(dosage.getDate_hour(), "N/D"));
        holder.tvPrescription.setText(MoreUtils.coalesce(dosage.getPrescription(),"N/D"));
        holder.tvPill.setText(MoreUtils.coalesce(dosage.getPill().getName(),"N/D"));
//        holder.tvHours.setText(MoreUtils.coalesce(dosage.getHour(),"N/D"));


//        if (this.treatment != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle b = new Bundle();
//                    b.putSerializable("doctor", new Gson().toJson(data.get(holder.getAdapterPosition())));
//                    b.putSerializable("treatment",treatment);
//
//                    try {
//                        Thread.sleep(250);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Navigation.findNavController(view).navigate(R.id.medicalTreatmentAdd, b);
//                }
//            });
//
//        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void filter(@NonNull String strSearch ){

        //final  List<Patient> original = this.originalData;

        if (strSearch.length() == 0){

            this.data.clear();
            this.data.addAll(this.originalData);

        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                List<Dosage> collect = this.originalData.stream()
                        .filter(p -> p.getPill().getName().toLowerCase().contains(strSearch.toLowerCase()))
                        .collect(Collectors.toList());
                this.data.clear();
                data.addAll(collect);
            }
            else {
                for (Dosage p: originalData){
                    if (p.getPill().getName().toLowerCase().contains(strSearch.toLowerCase())){
                        data.add(p);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }

    public class DosageViewHolder  extends RecyclerView.ViewHolder{

        TextView tvQuantity;
        TextView tvhoursDate;
        TextView tvPrescription;
        TextView tvPill;
        TextView tvHours;

        CardView cardView;

        public DosageViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuantity = itemView.findViewById(R.id.tvDosageQuantity);
            tvhoursDate = itemView.findViewById(R.id.tvDosageHours);
            tvPrescription = itemView.findViewById(R.id.tvDosagePrescription);
            tvPill = itemView.findViewById(R.id.tvDosagePillName);
       //     tvHours = itemView.findViewById(R.id.tvDosageHours);

            cardView = itemView.findViewById(R.id.cardViewDosage);

        }
    }
}
