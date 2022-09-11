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
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private MedicalTreatment treatment;

    private List<Doctor> data = new ArrayList<>();
    private  List<Doctor> originalData = new ArrayList<>();
    private Context context;


    public DoctorAdapter() {
    }

    public void setData(List<Doctor> data) {
        this.data = data;
        this.originalData.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<Doctor> data, MedicalTreatment treatment) {
        this.data = data;
        this.originalData.addAll(data);
        this.treatment = treatment;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DoctorViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_doctor, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {

        Doctor doctor = data.get(position);

        holder.tvName.setText(MoreUtils.coalesce(doctor.getName(), "N/D"));
        holder.tvSpecialism.setText(MoreUtils.coalesce(doctor.getSpecialism(), "N/D"));
        holder.tvPhoneNumber.setText(MoreUtils.coalesce(doctor.getPhoneNumber(),"N/D"));
        holder.tvEmail.setText(MoreUtils.coalesce(doctor.getEmail(),"N/D"));
        holder.tvDirection.setText(MoreUtils.coalesce(doctor.getDirection(),"N/D"));


        if (this.treatment != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putSerializable("doctor", new Gson().toJson(data.get(holder.getAdapterPosition())));
                    b.putSerializable("treatment",treatment);

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Navigation.findNavController(view).navigate(R.id.medicalTreatmentAdd, b);
                }
            });

        }

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

                List<Doctor> collect = this.originalData.stream()
                        .filter(p -> p.getName().toLowerCase().contains(strSearch.toLowerCase()))
                        .collect(Collectors.toList());
                this.data.clear();
                data.addAll(collect);
            }
            else {
                for (Doctor p: originalData){
                    if (p.getName().toLowerCase().contains(strSearch.toLowerCase())){
                        data.add(p);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }

    public class DoctorViewHolder  extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvSpecialism;
        TextView tvPhoneNumber;
        TextView tvEmail;
        TextView tvDirection;
        ImageView imgEvaluador;
        CardView cardView;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNameDoctor);
            tvSpecialism = itemView.findViewById(R.id.tvSpecialismDoctor);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumberDoctor);
            tvEmail = itemView.findViewById(R.id.tvDoctorEmail);
            tvDirection = itemView.findViewById(R.id.tvDoctorDirection);

            imgEvaluador = itemView.findViewById(R.id.imgEvaluador);
            cardView = itemView.findViewById(R.id.cardViewDoctor);

        }
    }
}
