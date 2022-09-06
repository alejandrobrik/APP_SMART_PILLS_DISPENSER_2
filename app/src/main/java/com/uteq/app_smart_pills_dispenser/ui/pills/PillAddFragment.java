package com.uteq.app_smart_pills_dispenser.ui.pills;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.models.Pill;
import com.uteq.app_smart_pills_dispenser.services.PatientService;
import com.uteq.app_smart_pills_dispenser.services.PillService;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PillAddFragment extends Fragment{
    PillService pillService;
    EditText txtAddPillName;
    EditText txtAddPillDescription;
    Button btnPillSave;

    private Dosage dosage;
    private MedicalTreatment treatment;

    public PillAddFragment()
    {
        super(R.layout.fragment_pill_add);
    }
    public void addPill(Pill pill)
    {
        pillService = Apis.getPillService();
        Call<Pill> call = pillService.addPill(pill);
        call.enqueue(new Callback<Pill>() {
            @Override
            public void onResponse(Call<Pill> call, Response<Pill> response) {
                if(response!=null) {
                    Toast.makeText(getContext(), "Successful registration.",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Pill> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            dosage = (Dosage) getArguments().getSerializable("dosage");
            treatment = (MedicalTreatment) getArguments().getSerializable("treatment");

        }

        txtAddPillName = view.findViewById(R.id.txtAddPillName);
        txtAddPillDescription = view.findViewById(R.id.txtAddPillDescription);
        btnPillSave = view.findViewById(R.id.btnPillSave);
        btnPillSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pill pill = new Pill();
                pill.setName(txtAddPillName.getText().toString());
                pill.setDescription(txtAddPillDescription.getText().toString());
                pill.setState((true));
                addPill(pill);

                Bundle bundle = new Bundle();
                bundle.putSerializable("dosage",dosage);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Navigation.findNavController(view).navigate(R.id.pillListFragment, bundle);
            }
        });
    }
}