package com.uteq.app_smart_pills_dispenser.ui.dosages;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.models.Pill;
import com.uteq.app_smart_pills_dispenser.services.DosageService;
import com.uteq.app_smart_pills_dispenser.services.MedicalTreatmentService;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.DatePickerFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DosageAddFragment extends Fragment {

    DosageService service;

    String doctorGson, patientGson;
    Pill pill;
    MedicalTreatment medicalTreatment;

    Dosage dosage;
    Dosage dosageCardview;

    Boolean stateDate;
    EditText txtQuantity;
    EditText txtStartDate;
    EditText txtEndDate;
    EditText txtHorus;
    TextView tvNameSelectedPill;
    Button btnSelectPill;
    Button btnAddDosages;

    public DosageAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            //  patientGson = getArguments().getString("patient");
            doctorGson = getArguments().getString("doctor");

            medicalTreatment = (MedicalTreatment) getArguments().getSerializable("treatment");


            dosageCardview = (Dosage) getArguments().getSerializable("dosage");


            pill = new Gson().fromJson(doctorGson, Pill.class);

            tvNameSelectedPill = view.findViewById(R.id.tvNameSelectedDoctor);

            if (pill != null)
                tvNameSelectedPill.setText("The doctor selected is: " + pill.getName());
            if(medicalTreatment == null)
                medicalTreatment = dosageCardview.getMedicalTreatment();
        }

        txtHorus = view.findViewById(R.id.txtDosageHours);
        txtStartDate = view.findViewById(R.id.txtDosageStartDate);
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDate = true;
                showDatePickerDialog();

            }
        });
        txtEndDate = view.findViewById(R.id.txtDosageEndDate);
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDate = false;
                showDatePickerDialog();
            }
        });


        btnSelectPill = view.findViewById(R.id.btnSelectPill);
        btnSelectPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosage = new Dosage();
                dosage.setHour(txtHorus.getText().toString());
                dosage.setStarDate(txtStartDate.getText().toString());
                dosage.setEndDate(txtEndDate.getText().toString());
                dosage.setQuantity(Integer.parseInt(txtQuantity.getText().toString()));
                dosage.setMedicalTreatment(medicalTreatment);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dosage", dosage);
                Navigation.findNavController(view).navigate(R.id.pillListFragment,bundle);
            }
        });

        btnAddDosages = view.findViewById(R.id.btnSaveDosage);
        btnAddDosages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosage = new Dosage();
                dosage.setHour(txtHorus.getText().toString());
                dosage.setStarDate(txtStartDate.getText().toString());
                dosage.setEndDate(txtEndDate.getText().toString());
                dosage.setQuantity(Integer.parseInt(txtQuantity.getText().toString()));
                dosage.setMedicalTreatment(dosageCardview.getMedicalTreatment());
                dosage.setPill(pill);
                addDosage(dosage);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Navigation.findNavController(view).navigate(R.id.nav_home);
            }
        });

        //Si la dosis que viajo es diferente de null seteamos los campos
        if (dosage!=null){
            txtHorus.setText(dosageCardview.getHour());
            txtStartDate.setText(dosageCardview.getStarDate());
            txtEndDate.setText(dosageCardview.getEndDate());
            txtQuantity.setText(dosageCardview.getQuantity());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dosage_add, container, false);
    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                // +1 because January is zero
                month = month +1;
                String monthParse;
                String dayParse;
                String selectedDate;

                if (month <10)
                    monthParse = "0"+month;
                else
                    monthParse = ""+month;
                if (day < 10)
                    dayParse = "0"+day;
                else
                    dayParse = ""+day;
                selectedDate = (year + "-" +monthParse +"-" + dayParse);

                if (stateDate)
                    txtStartDate.setText(selectedDate);
                else
                    txtEndDate.setText(selectedDate);

            }
        });

        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    public void addDosage(Dosage dosage) {
        service = Apis.getDosageService();
        Call<Dosage> call = service.addDosage(dosage);

        call.enqueue(new Callback<Dosage>() {
            @Override
            public void onResponse(Call<Dosage> call, Response<Dosage> response) {
                if (response != null) {
                    Toast.makeText(getContext(), "Successful registration.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Dosage> call, Throwable t) {
                Log.e("Error:", t.getMessage());

            }
        });
    }
}