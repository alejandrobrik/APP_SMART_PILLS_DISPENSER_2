package com.uteq.app_smart_pills_dispenser.ui.dosages;

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
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.adapters.DosageAdapter;
import com.uteq.app_smart_pills_dispenser.adapters.MedicalTreatmentAdapter;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.services.MedicalTreatmentService;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DosageListFragment extends Fragment implements SearchView.OnQueryTextListener {

    Button btnAddPatient;
    FloatingActionButton favNewDosage;

    private int id_carer;
    private Carer carer;
    MedicalTreatment treatment;

    String medicalTreatmentGson;
    MedicalTreatment treatmentDosageView;
    private Patient patient;

    Carer carerLogin = new Carer();

    private RecyclerView recyclerView;
    private SearchView svSearchDosage;
    private DosageAdapter dosageAdapter;
    private Dosage dosage;

    public DosageListFragment() {
        // Required empty public constructor
        super(R.layout.fragment_dosage_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            id_carer = getArguments().getInt("id_carer", 0);
            carer = getArguments().getParcelable("c");
            treatment = (MedicalTreatment) getArguments().getSerializable("treatment");
            dosage = (Dosage) getArguments().getSerializable("dosage");
            if (treatment == null && dosage != null)
                treatment = dosage.getMedicalTreatment();

            medicalTreatmentGson =  getArguments().getString("treatmentDosageView");
            if (medicalTreatmentGson != null)
                treatment = new Gson().fromJson(medicalTreatmentGson,MedicalTreatment.class);
           // treatmentDosageView = (MedicalTreatment) getArguments().getSerializable("treatmentDosageView");

        }

        recyclerView = view.findViewById(R.id.reciclerviewDosage);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        dosageAdapter = new DosageAdapter();
        recyclerView.setAdapter(dosageAdapter);

        svSearchDosage = view.findViewById(R.id.svSearchDosage);

        //Llama a un metodo del activity que toma el carer que inicio sesion
       // ((MenuActivity)getActivity()).loadData();

      //  carerLogin = ((MenuActivity)getActivity()).loadData();


        favNewDosage = view.findViewById(R.id.favNewDosage);
        favNewDosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("treatment", treatment);

                Navigation.findNavController(view).navigate(R.id.dosageAddFragment,bundle);

            }
        });




        try {
            getDosage();

        } catch (Exception e) {
            e.printStackTrace();
        }

        initListener();


    }

    public void getDosage() throws Exception {
        String id ="1";
        if (dosage!= null) {
            if (dosage.getMedicalTreatment().getId() != null)
                id = "" + dosage.getMedicalTreatment().getId();
        }
        else {
            id = "" + treatment.getId();
        }
        Call<List<Dosage>> dosageList = Apis.getDosageService().getDosage(id);

        dosageList.enqueue(new Callback<List<Dosage>>() {
            @Override
            public void onResponse(Call<List<Dosage>> call, Response<List<Dosage>> response) {
                if(response.isSuccessful()){
                    List <Dosage>  dosages = response.body();
                    dosageAdapter.setData(dosages);
                }
            }

            @Override
            public void onFailure(Call<List<Dosage>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }

    private  void initListener(){
        svSearchDosage.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        dosageAdapter.filter(newText);
        return false;
    }
}