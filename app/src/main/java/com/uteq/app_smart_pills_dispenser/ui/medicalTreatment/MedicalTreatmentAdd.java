package com.uteq.app_smart_pills_dispenser.ui.medicalTreatment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uteq.app_smart_pills_dispenser.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicalTreatmentAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicalTreatmentAdd extends Fragment {



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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medical_treatment_add, container, false);
    }
}