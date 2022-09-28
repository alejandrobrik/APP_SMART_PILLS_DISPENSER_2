package com.uteq.app_smart_pills_dispenser.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Dosage> data = new ArrayList<>();
    private  List<Dosage> originalData = new ArrayList<>();
    private Context context;


    public ScheduleAdapter() {
    }

    public void setData(List<Dosage> data) {
        this.data = data;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ScheduleAdapter.ScheduleViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_schedule, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleViewHolder holder, int position) {
        Dosage dosage = data.get(position);
        try {
            // String [] horario = dosage.getDateTake().split("T");


            //       holder.tvhoursDate.setText(MoreUtils.coalesce(horario[0] +"\n"+ horario[1].substring(0, 8), "N/D"));
            holder.tvdate.setText(MoreUtils.coalesce(dosage.getDateHour().substring(0,10),"N/D"));
            holder.tvhoursDate.setText(MoreUtils.coalesce(dosage.getDateHour().substring(11,16), "N/D"));
            holder.tvPill.setText(MoreUtils.coalesce(dosage.getPill().getName(), "N/D"));
            holder.tvQuantity.setText(MoreUtils.coalesce(String.valueOf(dosage.getQuantity()),"N/D"));



        }catch (Exception e) {
            System.out.print("El error es:" + e);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView tvdate;
        TextView tvhoursDate;
        TextView tvPill;
        TextView tvQuantity;


        CardView cardView;
        public ScheduleViewHolder(@NonNull View itemView) {

            super(itemView);

            tvdate = itemView.findViewById(R.id.tvScheduleDate);
            tvhoursDate = itemView.findViewById(R.id.tvScheduleHour);
            tvPill = itemView.findViewById(R.id.tvSchedulePill);
            tvQuantity = itemView.findViewById(R.id.tvScheduleQuantity);

            cardView = itemView.findViewById(R.id.cardViewSchedule);

        }

    }
}
