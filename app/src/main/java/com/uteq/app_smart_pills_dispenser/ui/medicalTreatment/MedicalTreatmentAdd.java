package com.uteq.app_smart_pills_dispenser.ui.medicalTreatment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.services.MedicalTreatmentService;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.DatePickerFragment;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.TimePickerFragment;
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
    //EditText txtEndDate;
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
        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showTimePickerDialog();
            }
        });
        txtStartDate = view.findViewById(R.id.txtTreatmentStartDate);
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDate = true;
                showDatePickerDialog();

            }
        });
        //txtEndDate = view.findViewById(R.id.txtTreatmentEndDate);



        btnSelectDoctor = view.findViewById(R.id.btnSelectDoctor);
        btnSelectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mt = new MedicalTreatment();
                mt.setDescription(txtDescription.getText().toString());
                mt.setStartDate(txtStartDate.getText().toString());
       //         mt.setEndDate(txtEndDate.getText().toString());
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
                mt.setStartDate(txtStartDate.getText().toString());
         //       mt.setEndDate(txtEndDate.getText().toString());
                mt.setPatient(patient);
                mt.setDoctor(doctor);

                if (doctor == null || mt.getStartDate() == null || mt.getDescription().isEmpty() ){
                    Toast.makeText(getContext(), "Please check the fields.", Toast.LENGTH_LONG).show();
                }
                else {
                    txtDescription.setText("");
                    txtStartDate.setText("");
                    getArguments().clear();


                    addMedicalTreatment(mt);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("treatment", mt);
                    bundle.putSerializable("patient", mt.getPatient());
                    setArguments(bundle);
                    Navigation.findNavController(view).navigate(R.id.dosageAddFragment, bundle);

                }
            }
        });

        if (mtCardview!=null){
                txtDescription.setText(mtCardview.getDescription());
                txtStartDate.setText(mtCardview.getStartDate());
           //     txtEndDate.setText(mtCardview.getEndDate());
        }

//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("patient", patient);
//                Navigation.findNavController(view).navigate(R.id.medicalTreatmentListFragment, bundle);
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(callback);


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


            }
        });

        newFragment.show(getChildFragmentManager(), "datePicker");
    }


    private void showTimePickerDialog() {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                String hourParse="";
                String minuteParse="";

                if (hour<10)
                    hourParse = "0"+hour;
                else
                    hourParse = ""+hour;

                if (minute<10)
                    minuteParse = "0"+minute;
                else
                    minuteParse = ""+minute;


                System.out.println("la hora que eligio es: " + hourParse + " y los minutos son: "+ minuteParse);
            }
        });

        newFragment.show(getChildFragmentManager(), "timePicker");
    }





    public void addMedicalTreatment(MedicalTreatment mt) {
        service = Apis.getMedicalTreatmentService();
        Call<MedicalTreatment> call = service.addMedicalTreatment(mt);

        call.enqueue(new Callback<MedicalTreatment>() {
            @Override
            public void onResponse(Call<MedicalTreatment> call, Response<MedicalTreatment> response) {
                if (response != null) {
                    //Aqui voy a guardar la respuesta del bodu
                    String bodyResponse;
                    response.body();
                    mt.setId(response.body().getId());
                    bodyResponse = response.body().toString();

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