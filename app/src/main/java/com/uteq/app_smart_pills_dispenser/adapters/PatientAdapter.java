package com.uteq.app_smart_pills_dispenser.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder>  {

    private List<Patient> data = new ArrayList<>();
    private Context context;


    public PatientAdapter() {
    }

    public void setData(List<Patient> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new PatientViewHolder(LayoutInflater.from(context).inflate(R.layout.carview_patient, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = data.get(position);

        holder.txtname.setText(MoreUtils.coalesce(patient.getName(), "N/D"));
        holder.txtgender.setText(MoreUtils.coalesce(patient.getGender(), "N/D"));
        holder.txtbirthDate.setText(MoreUtils.coalesce(patient.getBirth_date(), "N/D"));
    }


    public int getItemCount() {
        return data.size();
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder {

        TextView txtname;
        TextView txtgender;
        TextView txtbirthDate;
        ImageView imgEvaluador;
        CardView cardView;


        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            txtname = itemView.findViewById(R.id.tvNameCarer);
            txtgender = itemView.findViewById(R.id.tvGenderPatient);
            txtbirthDate = itemView.findViewById(R.id.tvbirthDatePatient);
            imgEvaluador = itemView.findViewById(R.id.imgEvaluador);
            cardView = itemView.findViewById(R.id.cardView);
        }


    }
}