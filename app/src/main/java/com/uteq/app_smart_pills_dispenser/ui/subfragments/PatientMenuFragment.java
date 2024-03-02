package com.uteq.app_smart_pills_dispenser.ui.subfragments;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.graphics.fonts.Font;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.ui.medicalTreatment.MedicalTreatmentListFragment;
import com.uteq.app_smart_pills_dispenser.ui.patients.PatientListFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    CardView cardViewReports;
    CardView cardViewScheduleMenu;

    CardView cardViewSettingsMenu;
    List <MedicalTreatment>  medicalTreatmentListReport;
    LinearLayout linearLayout;


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

        cardViewMedicalTreatment = view.findViewById(R.id.cardView);

        //linearLayout = view.findViewById(R.id.linearMedical);

        System.out.println("Aqui termino 2");
        cardViewMedicalTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("patient", patient);

                Navigation.findNavController(view).navigate(R.id.medicalTreatmentListFragment,bundle);
            }
        });

        try {
            getMedicalTreatment();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cardViewReports = view.findViewById(R.id.cardView2);
        cardViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("patient", patient);
                Navigation.findNavController(view).navigate(R.id.reportPatientFragment, bundle);

            }
        });
        cardViewScheduleMenu = view.findViewById(R.id.cardView3);
        cardViewScheduleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("patient", patient);
                Navigation.findNavController(view).navigate(R.id.scheduleFragment, bundle);
            }
        });

        cardViewSettingsMenu = view.findViewById(R.id.cardView4);
        cardViewSettingsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("patient", patient);
                Navigation.findNavController(view).navigate(R.id.action_patientMenuFragment_to_patientSettingsFragment2, bundle);
            }
        });
    }


    public void getMedicalTreatment() throws Exception {

        String id = ""+patient.getId();
        Call<List<MedicalTreatment>> medicalTreatmentList = Apis.getMedicalTreatmentService().getMedicalTreatment(id);

        medicalTreatmentList.enqueue(new Callback<List<MedicalTreatment>>() {
            @Override
            public void onResponse(Call<List<MedicalTreatment>> call, Response<List<MedicalTreatment>> response) {
                if(response.isSuccessful()){
                     medicalTreatmentListReport = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<MedicalTreatment>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_menu, container, false);
    }
}