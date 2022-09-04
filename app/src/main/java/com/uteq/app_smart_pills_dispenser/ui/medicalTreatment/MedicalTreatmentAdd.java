package com.uteq.app_smart_pills_dispenser.ui.medicalTreatment;

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
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.services.MedicalTreatmentService;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.DatePickerFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedicalTreatmentAdd extends Fragment {

    MedicalTreatmentService service;

    String doctorGson, patientGson;
    Doctor doctor;
    Patient patient;

    MedicalTreatment mt;
    MedicalTreatment mtCardview;

    Boolean stateDate;
    EditText txtDescription;
    EditText txtStartDate;
    EditText txtEndDate;
    TextView tvNameSelectedDoctor;
    Button btnSelectDoctor;
    Button btnAddDosages;

    public MedicalTreatmentAdd() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            //  patientGson = getArguments().getString("patient");
            doctorGson = getArguments().getString("doctor");

            patient = (Patient) getArguments().getSerializable("patient");




            mtCardview = (MedicalTreatment) getArguments().getSerializable("treatment");


            doctor = new Gson().fromJson(doctorGson, Doctor.class);

            tvNameSelectedDoctor = view.findViewById(R.id.tvNameSelectedDoctor);

            if (doctor != null)
                tvNameSelectedDoctor.setText("The doctor selected is: " + doctor.getName());
            if(patient == null)
                patient = mtCardview.getPatient();
        }




        txtDescription = view.findViewById(R.id.txtTreatmentDescription);
        txtStartDate = view.findViewById(R.id.txtTreatmentStartDate);
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDate = true;
                showDatePickerDialog();

            }
        });
        txtEndDate = view.findViewById(R.id.txtTreatmentEndDate);
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDate = false;
                showDatePickerDialog();
            }
        });


        btnSelectDoctor = view.findViewById(R.id.btnSelectDoctor);
        btnSelectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mt = new MedicalTreatment();
                mt.setDescription(txtDescription.getText().toString());
                mt.setStart_Date(txtStartDate.getText().toString());
                mt.setEndDate(txtEndDate.getText().toString());
                mt.setPatient(patient);
                Bundle bundle = new Bundle();
                bundle.putSerializable("treatment", mt);
                Navigation.findNavController(view).navigate(R.id.doctorListSelectFragment,bundle);
            }
        });

        btnAddDosages = view.findViewById(R.id.btnMedicalTreatmentSave);
        btnAddDosages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mt = new MedicalTreatment();
                mt.setDescription(txtDescription.getText().toString());
                mt.setStart_Date(txtStartDate.getText().toString());
                mt.setEndDate(txtEndDate.getText().toString());
                mt.setPatient(mtCardview.getPatient());
                mt.setDoctor(doctor);
                addMedicalTreatment(mt);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Navigation.findNavController(view).navigate(R.id.dosageListFragment);
            }
        });

        if (mtCardview!=null){
                txtDescription.setText(mtCardview.getDescription());
                txtStartDate.setText(mtCardview.getStart_Date());
                txtEndDate.setText(mtCardview.getEndDate());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medical_treatment_add, container, false);
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

    public void addMedicalTreatment(MedicalTreatment mt) {
        service = Apis.getMedicalTreatmentService();
        Call<MedicalTreatment> call = service.addMedicalTreatment(mt);

        call.enqueue(new Callback<MedicalTreatment>() {
            @Override
            public void onResponse(Call<MedicalTreatment> call, Response<MedicalTreatment> response) {
                if (response != null) {
                    Toast.makeText(getContext(), "Successful registration.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MedicalTreatment> call, Throwable t) {
                Log.e("Error:", t.getMessage());

            }
        });
    }
}