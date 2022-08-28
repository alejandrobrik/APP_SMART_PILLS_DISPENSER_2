package com.uteq.app_smart_pills_dispenser.ui.subfragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.services.PatientService;
import com.uteq.app_smart_pills_dispenser.ui.patients.GalleryFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PatientAddFragment extends Fragment {


    PatientService service;

    EditText txtname;
    Spinner  spinerGenderPatient;
    EditText txtbirthdate;
    EditText txtpassword;
    EditText txtRepeatPassword;
    String genero;

    String [] generos = {"Male", "Femelale", "No binary"};

    Button btnBack;
    Button save;
    Button btnClean;

    public PatientAddFragment() {
        super(R.layout.fragment_patient_add);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtname = view.findViewById(R.id.txtNamePatient);
        txtbirthdate = view.findViewById(R.id.txtBirthDatePatient);
        spinerGenderPatient = (Spinner) view.findViewById(R.id.spinerGenderPatient);

        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(getContext(),R.array.combo_gender, android.R.layout.simple_spinner_item);

        spinerGenderPatient.setAdapter(adapter);
        spinerGenderPatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                genero = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtpassword = view.findViewById(R.id.txtpassword);
        txtRepeatPassword = view.findViewById(R.id.txtrepeatPassword);



        save = view.findViewById(R.id.btnSavePatient);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient p = new Patient();
                p.setName(txtname.getText().toString());
                p.setBirth_date(txtbirthdate.getText().toString());
                p.setGender(genero);
                p.setState((true));

                addPatient(p);

            }
        });

        btnBack = view.findViewById(R.id.btnBackPatient);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GalleryFragment.class);
                startActivity(intent);
            }
        });

        }


    public void addPatient(Patient p)
    {
        service = Apis.getPatientService();
        Call<Patient> call = service.addPatient(p);

        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response!=null) {
                    Toast.makeText(getContext(), "Successful registration.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e("Error:",t.getMessage());

            }
        });
    }
    }


