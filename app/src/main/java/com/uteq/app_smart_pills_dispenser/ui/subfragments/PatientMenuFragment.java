package com.uteq.app_smart_pills_dispenser.ui.subfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.ui.medicalTreatment.MedicalTreatmentListFragment;
import com.uteq.app_smart_pills_dispenser.ui.patients.PatientListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String patientGson ;
    Patient patient;
    TextView patientTitle;
    CardView cardViewMedicalTreatment;


    public PatientMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientMenuFragment newInstance(String param1, String param2) {
        PatientMenuFragment fragment = new PatientMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            patientGson =  getArguments().getString("patient");

        }

        patient = new Gson().fromJson(patientGson,Patient.class);

        patientTitle = view.findViewById(R.id.textViewPatientTitle);

        patientTitle.setText("Patient: "+ patient.getName());

        System.out.println("Aqui termino");

        cardViewMedicalTreatment = view.findViewById(R.id.cardViewMedicalTreatment);
        cardViewMedicalTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                MedicalTreatmentListFragment medicalTreatmentListFragment = new MedicalTreatmentListFragment();
                bundle.putString("id_patient", patient.getId());
                medicalTreatmentListFragment.setArguments(bundle);
                Navigation.findNavController(view).navigate(R.id.medicalTreatmentListFragment,bundle);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_menu, container, false);
    }
}