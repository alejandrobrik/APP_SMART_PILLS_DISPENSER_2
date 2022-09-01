package com.uteq.app_smart_pills_dispenser.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    @NonNull
    @Override
    public DoctorAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.DoctorViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DoctorViewHolder  extends RecyclerView.ViewHolder{
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
