package com.uteq.app_smart_pills_dispenser.ui.patients;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.adapters.PatientAdapter;
import com.uteq.app_smart_pills_dispenser.databinding.FragmentPatientAddBinding;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientListFragment extends Fragment implements  SearchView.OnQueryTextListener {

    Button btnAddPatient;
    Button btnViewAll;
    FloatingActionButton favNewPatient;
    FragmentTransaction transaction;
    Fragment patientAddFragment;

    CardView cardViewPatient;

    private int id_carer;
    private Carer carer;

    String cadenaRespuesta;

    Carer carerLogin = new Carer();

    private RecyclerView recyclerView;
    private SearchView svSearchPatient;
    private PatientAdapter patientAdapter;

    public PatientListFragment() {
        super(R.layout.fragment_patient_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        deleteCache(getContext());
        if (getArguments() != null) {
            id_carer = getArguments().getInt("id_carer", 0);
            carer = getArguments().getParcelable("c");
        }



        recyclerView = view.findViewById(R.id.reciclerviewPatient);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        patientAdapter = new PatientAdapter();
        recyclerView.setAdapter(patientAdapter);

        svSearchPatient = view.findViewById(R.id.svSearchPatient);

        //Llama a un metodo del activity que toma el carer que inicio sesion
        ((MenuActivity)getActivity()).loadData();

        carerLogin = ((MenuActivity)getActivity()).loadData();

//       FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//       FragmentTransaction transaction =fragmentManager.beginTransaction();
//       transaction.setReorderingAllowed(true);

        favNewPatient = view.findViewById(R.id.favNewPatient);
        favNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("id_login", carerLogin);


                Navigation.findNavController(view).navigate(R.id.patientAddFragment,bundle);

                //  ((MenuActivity)getActivity()).optionSelect();


                return;
//                Intent intent = new Intent(getContext(), MenuActivity.class);
//                startActivity(intent);

            }
        });




        try {
            getpatient();

            getUltimoId();


        } catch (Exception e) {
            e.printStackTrace();
        }

        initListener();

    }

    public void getpatient() throws Exception {

        String id = ""+carerLogin.getId();
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

    public String getUltimoId(){
        Call<String> ultimo = Apis.getMedicalTreatmentService().getMedicalTreatmentLastId();
        ultimo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    String respuesta = response.body();
                    cadenaRespuesta = respuesta;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        return "hola";
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
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

        patientAdapter.filter(newText);
        return false;
    }
}