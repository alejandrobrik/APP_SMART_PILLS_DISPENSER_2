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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.models.Pill;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.PillViewHolder> {
    private Dosage dosage;

    private List<Pill> data = new ArrayList<>();
    private List<Pill> originalData = new ArrayList<>();
    private Context context;

    public PillAdapter() {
    }

    public void setData(List<Pill> data) {
        this.data = data;
        this.originalData.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<Pill> data, Dosage dosage) {
        this.data = data;
        this.originalData.addAll(data);
        this.dosage = dosage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PillAdapter.PillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new PillAdapter.PillViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_pill, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull PillAdapter.PillViewHolder holder, int position) {
        Pill pill = data.get(position);
        holder.txtName.setText(MoreUtils.coalesce(pill.getName(), "N/D"));
        holder.txtDescription.setText(MoreUtils.coalesce(pill.getDescription(), "N/D"));

        Glide.with(context)
                .load(pill.getUrlImage())
                .error(R.drawable.ic_pill)
                .into(holder.imgPill);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("pill", new Gson().toJson(data.get(holder.getAdapterPosition())));
                bundle.putSerializable("dosage", dosage);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Navigation.findNavController(view).navigate(R.id.action_pillListFragment_to_dosageAddFragment, bundle);
            }
        });
    }

    public int getItemCount() {
        return data.size();
    }

    public void filter(@NonNull String strSearch) {
        if (strSearch.length() == 0) {

            this.data.clear();
            this.data.addAll(this.originalData);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                List<Pill> collect = this.originalData.stream()
                        .filter(pill -> pill.getName().toLowerCase().contains(strSearch.toLowerCase()))
                        .collect(Collectors.toList());
                this.data.clear();
                data.addAll(collect);
            } else {
                for (Pill pill : originalData) {
                    if (pill.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                        data.add(pill);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public class PillViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtDescription;
        ImageView imgPill;
        CardView cardViewPill;

        public PillViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.tvListPillName);
            txtDescription = itemView.findViewById(R.id.tvListPillDescription);
            imgPill = itemView.findViewById(R.id.imgPill);
            cardViewPill = itemView.findViewById(R.id.cardViewPill);
        }


    }
}
