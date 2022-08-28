package com.uteq.app_smart_pills_dispenser.ui.patients;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.adapters.PatientAdapter;
import com.uteq.app_smart_pills_dispenser.databinding.FragmentGalleryBinding;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.ui.subfragments.PatientAddFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {
    Button btnSearch;
    Button btnAddPatient;
    Button btnViewAll;
    FloatingActionButton favNewPatient;
    FragmentTransaction transaction;
    Fragment patientAddFragment;

    private RecyclerView recyclerView;
    private PatientAdapter patientAdapter;

    public GalleryFragment() {
        super(R.layout.fragment_gallery);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.reciclerviewPatient);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        patientAdapter = new PatientAdapter();
        recyclerView.setAdapter(patientAdapter);


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        favNewPatient = view.findViewById(R.id.favNewPatient);
        favNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onDestroyView();
                ((MenuActivity)getActivity()).optionSelect();


                return;
//                Intent intent = new Intent(getContext(), MenuActivity.class);
//                startActivity(intent);

            }
        });


        try {
            getpatient();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getpatient() throws Exception {
        String id= "1";
        Call<List<Patient>> patientList = Apis.getPatientService().getPatient(id);

        patientList.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if(response.isSuccessful()){
                    List <Patient>  patients = response.body();
                    patientAdapter.setData(patients);
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }



}