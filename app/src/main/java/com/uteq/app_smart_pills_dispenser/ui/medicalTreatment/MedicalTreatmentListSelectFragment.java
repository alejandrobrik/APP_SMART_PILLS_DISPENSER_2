package com.uteq.app_smart_pills_dispenser.ui.medicalTreatment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.adapters.MedicalTreatmentAdapter;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicalTreatmentListSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicalTreatmentListSelectFragment extends Fragment implements  SearchView.OnQueryTextListener {

    Button btnAddPatient;
    FloatingActionButton favNewPatient;

    private int id_carer;
    private Carer carer;
    private Patient patient;

    Carer carerLogin = new Carer();

    private RecyclerView recyclerView;
    private SearchView svSearchPatient;
    private MedicalTreatmentAdapter medicalTreatmentAdapter;

    public MedicalTreatmentListSelectFragment() {
        super(R.layout.fragment_medical_treatment_list_select);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            id_carer = getArguments().getInt("id_carer", 0);
            carer = getArguments().getParcelable("c");
            patient = (Patient) getArguments().getSerializable("patient");
        }

        recyclerView = view.findViewById(R.id.reciclerviewMedicalTreatment);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        medicalTreatmentAdapter = new MedicalTreatmentAdapter();
        recyclerView.setAdapter(medicalTreatmentAdapter);

        svSearchPatient = view.findViewById(R.id.svSearchMedicalTreatment);

        //Llama a un metodo del activity que toma el carer que inicio sesion
        ((MenuActivity)getActivity()).loadData();

        carerLogin = ((MenuActivity)getActivity()).loadData();


        favNewPatient = view.findViewById(R.id.favNewMedicalTreatment);
        favNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("patient", patient);


                Navigation.findNavController(view).navigate(R.id.medicalTreatmentAdd,bundle);

            }
        });

        try {
            getMedicalTreatment();

        } catch (Exception e) {
            e.printStackTrace();
        }

        initListener();

    }

    public void getMedicalTreatment() throws Exception {

        String id = ""+patient.getId();
        Call<List<MedicalTreatment>> medicalTreatmentList = Apis.getMedicalTreatmentService().getMedicalTreatment(id);

        medicalTreatmentList.enqueue(new Callback<List<MedicalTreatment>>() {
            @Override
            public void onResponse(Call<List<MedicalTreatment>> call, Response<List<MedicalTreatment>> response) {
                if(response.isSuccessful()){
                    List <MedicalTreatment>  medicalTreatmentList = response.body();
                    medicalTreatmentAdapter.setData(medicalTreatmentList);
                }
            }

            @Override
            public void onFailure(Call<List<MedicalTreatment>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }

    private  void initListener(){
        svSearchPatient.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        medicalTreatmentAdapter.filter(newText);
        return false;
    }
}