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


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder>  {

    private List<Patient> data = new ArrayList<>();
    private  List<Patient> originalData = new ArrayList<>();
    private Context context;


    public PatientAdapter() {
    }

    public void setData(List<Patient> data) {
        this.data = data;
        this.originalData.addAll(data);
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
        holder.txtbirthDate.setText(MoreUtils.coalesce(patient.getBirthDate(), "N/D"));

//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.patientMenuFragment);
//
////                Intent intent = new Intent(context, EvaluadosActivity.class);
////                Bundle b = new Bundle();
////                b.putString("Evaluador", new Gson().toJson(data.get(holder.getAdapterPosition())));
////                intent.putExtras(b);
////                context.startActivity(intent);
//                //Toast.makeText(context, evaluador.getNombres(), Toast.LENGTH_SHORT).show();
//            }
//        };
//        holder.imgEvaluador.setOnClickListener(listener);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putSerializable("patient",new Gson().toJson(data.get(holder.getAdapterPosition())));

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Navigation.findNavController(view).navigate(R.id.patientMenuFragment,b);
            }
        });

        if (patient.getGender() != null){
        if (patient.getGender().equals("Female")) {
            Glide.with(context)
                    .load(patient.getUrlImage())
                    .error(R.drawable.ic_female)
                    .into(holder.imgPatient);
        }
        if (patient.getGender().equals("Male")){
            Glide.with(context)
                    .load(patient.getUrlImage())
                    .error(R.drawable.ic_male)
                    .into(holder.imgPatient);
        }
        }


    }



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

                List<Patient> collect = this.originalData.stream()
                        .filter(p -> p.getName().toLowerCase().contains(strSearch.toLowerCase()))
                        .collect(Collectors.toList());
                this.data.clear();
                data.addAll(collect);
            }
            else {
                for (Patient p: originalData){
                    if (p.getName().toLowerCase().contains(strSearch.toLowerCase())){
                        data.add(p);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder {

        TextView txtname;
        TextView txtgender;
        TextView txtbirthDate;
        ImageView imgPatient;
        CardView cardView;


        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            txtname = itemView.findViewById(R.id.tvNameCarer);
            txtgender = itemView.findViewById(R.id.tvGenderPatient);
            txtbirthDate = itemView.findViewById(R.id.tvbirthDatePatient);
            imgPatient = itemView.findViewById(R.id.imgPatient);
            cardView = itemView.findViewById(R.id.cardView);
        }


    }
}