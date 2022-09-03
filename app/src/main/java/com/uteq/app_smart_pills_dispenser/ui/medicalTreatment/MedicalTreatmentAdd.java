package com.uteq.app_smart_pills_dispenser.ui.medicalTreatment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uteq.app_smart_pills_dispenser.R;


public class MedicalTreatmentAdd extends Fragment {


    Button selectDoctor;

    public MedicalTreatmentAdd() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MedicalTreatmentAdd newInstance(String param1, String param2) {
        MedicalTreatmentAdd fragment = new MedicalTreatmentAdd();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {

        }

        selectDoctor = view.findViewById(R.id.btnSelectDoctor);

        selectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_doctors);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medical_treatment_add, container, false);
    }
}