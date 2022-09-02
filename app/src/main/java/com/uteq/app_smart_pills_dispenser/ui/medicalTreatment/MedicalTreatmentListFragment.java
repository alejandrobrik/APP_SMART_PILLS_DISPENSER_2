package com.uteq.app_smart_pills_dispenser.ui.medicalTreatment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Patient;


public class MedicalTreatmentListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String patient;

    public MedicalTreatmentListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MedicalTreatmentListFragment newInstance(String param1, String param2) {
        MedicalTreatmentListFragment fragment = new MedicalTreatmentListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            patient = getArguments().getString("id_patient");
            System.out.println(patient);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medical_treatment_list, container, false);
    }
}